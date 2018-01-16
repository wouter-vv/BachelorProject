<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

/**
 * Class Breweries
 * contains data of all the Breweries
 * @package App
 */
class Values extends Model
{
    protected $table = 'values';
    protected $fillable = ['name', 'valueBeacon1', 'valueBeacon2', 'valueBeacon3', 'valueBeacon4' ];
    public function rooms()
    {
        return $this->hasOne('App\Rooms');
    }
}
