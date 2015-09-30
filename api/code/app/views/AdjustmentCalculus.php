<?php

namespace Notnull\Revis\Models;

use Notnull\Revis\Exceptions;

class AdjustmentCalculus
{
    const PRECISION = 6;

    private $work = null;
    private $months = null;
    private $autos = null;
    private $isDefinitive = true;

    public function __construct(Works $work)
    {
        $this->work = $work;

        if (!$this->work->formulaId || count($this->work->contracts) == 0) {
            throw new \Exception("Invalid Work");
        }
    }

    private function round($value)
    {
        return round($value, self::PRECISION);
    }

    public function monthIndexes()
    {
        // Get Work Formula
        $formula = PredefinedFormulas::findById($this->work->formulaId);

        // Get Reference Date

        $startDate = new \DateTime();
        $startDate->setTimestamp($this->work->contracts[0]['bidDate']);
        $startDate->sub(new \DateInterval('P1M'));
        $startDate->modify('first day of this month');

        // Get List Of Months

        $this->months = array_merge(
            [Date::getMonthArray($startDate->getTimestamp())],
            Date::getMonthsArray(
                $this->work->contracts[0]['consignmentDate'],
                $this->work->getEndDate()
            )
        );

        // Calculate Months

        foreach ($this->months as $i => &$monthInfo) {
            // Get Index Values for the Month
            $monthValues = IndexValues::findFirst([
                'conditions' => [
                    'month' => $monthInfo['timestamp'],
                    'location' => (int)$this->work->location
                ]
            ]);

            // If doesn't exist = Get Last Month Possible
            if (!$monthValues) {
                $this->isDefinitive = false;
                $monthValues = IndexValues::findFirst([
                    'conditions' => ['location' => $this->work->location],
                    'sort' => ['month' => -1]
                ]);
            }

            // Constant
            $monthInfo['constant'] = $formula->constant;

            // Indexes And Coefs

            $monthInfo['indexes'] = [];
            $monthInfo['coefs'] = [];

            foreach ($formula->coefs as $index => $value) {
                $monthInfo['indexes'][$index] = $monthValues->indexes[$index];

                // Base Month ?
                if ($i == 0) {
                    $monthInfo['coefs'][$index] = $value;
                    continue;
                }

                // Next Months
                $baseCoef = $this->months[0]['coefs'][$index];
                $baseIndex = $this->months[0]['indexes'][$index];
                $monthInfo['coefs'][$index] = $this->round(
                    ($monthValues->indexes[$index] * $baseCoef) / $baseIndex
                );
            }

            $monthInfo['coef'] = $this->round(
                array_sum($monthInfo['coefs']) + $monthInfo['constant']
            );

        }
        unset($monthInfo);

        return $this->months;
    }

    public function autosDecomposition($contractIndex)
    {
        if (!isset($this->work->contracts[$contractIndex])) {
            throw new \Exception("Contrato não existente.");
        }

        // Get Contract
        $contract =& $this->work->contracts[$contractIndex];

        // Get Payments Plan
        $paymentsPlan = [];
        ksort($contract['paymentsPlan']);
        foreach ($contract['paymentsPlan'] as $timestamp => $value) {
            $paymentsPlan[] = ['month' => $timestamp, 'value' => $value];
        }

        // Get Autos
        $decomposedAutos = $contract['autos'];

        $cleanPP = function (&$paymentsPlan) {
            while (count($paymentsPlan) > 0 && $paymentsPlan[0]['value'] <= 0) {
                array_splice($paymentsPlan, 0, 1);
            }
        };

        foreach ($decomposedAutos as &$auto) {

            $auto['_value'] = $auto['value'];
            $auto['label'] = Date::toMonthlyString($auto['referredMonth']);

            while ($auto['_value'] > 0) {

                // Remove Empty value months
                $cleanPP($paymentsPlan);

                // If Months Before
                if (isset($paymentsPlan[0]) && $paymentsPlan[0]['month'] < $auto['referredMonth']) {
                    $ppMonth = $paymentsPlan[0]['month'];
                    $value = min($paymentsPlan[0]['value'], $auto['_value']);
                    $paymentsPlan[0]['value'] -= $value;

                // If Other Months
                } else {
                    $ppMonth = $auto['referredMonth'];
                    $value = $auto['_value'];

                    $_value = $value;
                    while ($_value > 0) {
                        if (isset($paymentsPlan[0])) {
                            $tmp = min($_value, $paymentsPlan[0]['value']);
                            $paymentsPlan[0]['value'] -= $tmp;
                        } else {
                            $tmp = $_value;
                        }
                        $_value -= $tmp;
                        $cleanPP($paymentsPlan);
                    }
                }

                // Finalize
                $auto['_value'] -= $value;
                $auto['chunks'][] = [
                    'month' => $ppMonth,
                    'label' => Date::toMonthlyString($ppMonth),
                    'value' => $value
                ];
            }
        }

        $this->autos[$contractIndex] = $decomposedAutos;

        return $decomposedAutos;
    }

}
