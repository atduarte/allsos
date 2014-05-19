<?php

namespace Notnull\Revis\Models;

class Settings
{
    public static function isValidLocation($value) {
        $valid = function($v) { return ($v == 1 || $v == 0); };

        if (is_array($value)) {
            foreach ($value as $val) {
                if (!$valid($val)) {
                    return false;
                }
            }
            return true;
        }

        return $valid($value);
    }


    public static function locationName($id) {
        if ($id == 0) {
            return "Continente e Açores";
        } else if ($id == 1) {
            return "Madeira";
        } else {
            return "Localização Inválida";
        }
    }

}
