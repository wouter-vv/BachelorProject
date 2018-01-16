<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

/**
 * Class Breweries
 * contains data of all the Breweries
 * @package App
 */
class Devices extends Model
{
    protected $table = 'devices';
    protected $fillable = ['nameRoom'];
    public function rooms()
    {
        return $this->hasOne('App\Rooms');
    }
}
