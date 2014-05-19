<?php
/**
 * Created by IntelliJ IDEA.
 * User: atduarte
 * Date: 20-03-2014
 * Time: 13:43
 */

namespace Notnull\Revis\Models;

use Notnull\Revis\Exceptions;

class MySubMongo extends MyMongo
{
    public function save()
    {
        throw new \Exception();
    }
}
