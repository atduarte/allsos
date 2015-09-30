<?php

namespace Notnull\Revis\Models;

class Helpers
{
    public static function getContractName($i)
    {
        if ($i == 0) {
            return "Contrato Inicial";
        } else {
            return "Contrato Adicional " . $i;
        }
    }

    public static function price($value)
    {
        return number_format($value, 2, ',', '.') . " €";
    }

    public static function term($value)
    {
        if (!isset($value["number"]) || !isset($value["type"])) {
            return null;
        }

        $type = function ($number, $type) {
            switch ($type) {
                case 'M':
                    return $number == 1 ? 'Mês' : "Meses";
                case 'W':
                    return $number == 1 ? 'Semana' : "Semanas";
                case 'D':
                    return $number == 1 ? 'Dia' : "Dias";
                default:
                    return null;
            }
        };

        return $value["number"] . " " . $type($value["number"], $value["type"]);
    }
}
