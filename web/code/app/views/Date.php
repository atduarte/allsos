<?php

namespace Notnull\Revis\Models;

use Notnull\Revis\Exceptions;

class Date
{
    const FULLDATE_FORMAT = "d/m/Y|";
    const MONTH_FORMAT = "m/Y|";

    public static function toTimestamp($date)
    {
        if (!$date) {
            return null;
        }

        $date = \DateTime::createFromFormat(self::FULLDATE_FORMAT, $date);
        return $date->getTimestamp();
    }

    public static function toMonthlyTimestamp($date)
    {
        if (!$date) {
            return null;
        }

        $date = \DateTime::createFromFormat(self::MONTH_FORMAT, $date);
        return $date->getTimestamp();
    }

    public static function toString($timestamp)
    {
        if (!$timestamp) {
            return null;
        }

        $date = new \DateTime;
        $date->setTimestamp($timestamp);
        return $date->format(trim(self::FULLDATE_FORMAT, "|"));
    }

    public static function toMonthlyString($timestamp)
    {
        if (!$timestamp) {
            return null;
        }

        $date = new \DateTime;
        $date->setTimestamp($timestamp);
        return $date->format(trim(self::MONTH_FORMAT, "|"));

        /*$date = new \DateTime;
        $date->setTimestamp($timestamp);

        $monthNames = [
            1 => 'Janeiro',
            'Fevereiro',
            'Março',
            'Abril',
            'Maio',
            'Junho',
            'Julho',
            'Agosto',
            'Setembro',
            'Outubro',
            'Novembro',
            'Dezembro'
        ];

        return $monthNames[(int)$date->format('m')] . ' ' . $date->format('Y');*/
    }

    public static function lastSecondOfMonth($timestamp)
    {
        return strtotime("+1 day", strtotime('last day of this month', $timestamp))-1;
    }

    public static function addTerm($timestamp, $name)
    {
        $date = new \DateTime();
        $date->setTimestamp($timestamp);
    }

    public static function getMonthsArray($_start, $_end)
    {
        // Swap?
        if ($_start > $_end) {
            list($_end, $_start) = [$_start, $_end];
        }

        $start = new \DateTime();
        $start->setTimestamp($_start);
        $start->modify('first day of this month');

        $end = new \DateTime();
        $end->setTimestamp($_end);
        $end->modify('last day of this month');

        $interval = \DateInterval::createFromDateString('1 month');
        $period = new \DatePeriod($start, $interval, $end);

        $result = [];
        foreach ($period as $dt) {
            $timestamp = $dt->getTimestamp();
            $result[] = self::getMonthArray($timestamp);
        }

        return $result;
    }

    public static function getMonthArray($timestamp)
    {
        return [
            'timestamp' => $timestamp,
            'label' => self::toMonthlyString($timestamp)
        ];
    }
}
