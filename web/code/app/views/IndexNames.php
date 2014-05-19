<?php

namespace Notnull\Revis\Models;

use Notnull\Revis\Exceptions;

class IndexNames extends MyMongo
{
    public $name = null;
    public $slug = null;

    public function validation()
    {
        $this->requireField("name");
        $this->requireField("slug");
        $this->uniqueField("name");
        $this->uniqueField("slug");
    }

    public static function get()
    {
        return self::find([
            'sort' => ['slug' => 1, 'name' => 1]
        ]);
    }
}
