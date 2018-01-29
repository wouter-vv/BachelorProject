<?php

namespace App\Http\Controllers;

use App\Devices;
use App\Rooms;
use App\Users;
use Illuminate\Http\Request;

class RoomsController extends Controller
{
    public function index()
    {
        $rooms = Rooms::get();

        return view('rooms', array('rooms' => $rooms));
    }
    public function detail($id)
    {
        $room = Rooms::findOrFail($id);
        $devices = $room->devices;
        $mvalues = $room->measureValues()->orderBy('moment', 'desc')->paginate(25);
        return view('roomDetail', array('room' => $room, 'beacons' => $devices, 'mvalues' => $mvalues));
    }
}
