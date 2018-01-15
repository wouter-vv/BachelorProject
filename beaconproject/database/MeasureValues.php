<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

/**
 * Class Breweries
 * contains data of all the Breweries
 * @package App
 */
class MeasureValues extends Model
{
    protected $table = 'breweries';
    protected $fillable = ['name', 'foundingyear' ];
    public function beers()
    {
        return $this->hasMany('App\Beers', 'brewery_id');
    }
}
