<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class Values extends Model
{

    protected $table = 'measureValues';

    public function rooms()
    {
        return $this->hasOne('App\Rooms');
    }
}
