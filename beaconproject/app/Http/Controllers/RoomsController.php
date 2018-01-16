<?php

namespace App\Http\Controllers;

use App\Devices;
use App\Rooms;
use App\Users;
use Illuminate\Http\Request;

class RoomsController extends Controller
{
    /**
     * show the breweries
     */
    public function __construct()
    {
        $this->middleware('auth');
    }

    public function index()
    {
        $rooms = Rooms::get();

        return view('rooms', array('rooms' => $rooms));
    }
    public function detail($id)
    {
        $room = Rooms::findOrFail($id);
        $devices = $room->devices;
        return view('roomDetail', array('room' => $room, 'beacons' => $devices));
    }
}
