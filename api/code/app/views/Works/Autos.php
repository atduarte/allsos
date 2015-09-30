<?php

namespace Notnull\Revis\Models\Works;

use Notnull\Revis\Exceptions\InvalidField;
use Notnull\Revis\Models\MySubMongo;

class Autos extends MySubMongo
{
    const NAME_MAXSIZE = 4;
    public $creationDate = null;
    public $name;
    public $value;
    public $referredMonth;

    public function validation()
    {
        // Verify Creation Date
        $this->requireField("creationDate");
        $this->positiveField("creationDate", false);

        // Verify Name
        $this->requireField("name");
        if (count($this->name) > self::NAME_MAXSIZE) {
            throw new InvalidField("name");
        }

        // Verify Value
        $this->requireField("value");
        $this->positiveField("value", true);

        // Verify Referred Month
        $this->requireField("referredMonth");
        $this->positiveField("referredMonth", false);
    }
}
