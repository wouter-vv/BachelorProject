<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

/**
 * Class Breweries
 * contains data of all the Breweries
 * @package App
 */
class Rooms extends Model
{

    protected $table = 'rooms';
    protected $fillable = ['nameRoom'];
    public function devices()
    {
        return $this->hasMany('App\Devices', 'rooms_id');
    }

    public function values()
    {
        return $this->hasMany('App\Values');
    }
}
