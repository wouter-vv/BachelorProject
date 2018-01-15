{{-- extend the parent tpl --}}
@extends('layouts.master')
{{-- set the pagetitle in the parent tpl --}}
@section('title', 'Bierologie &raquo; Beer')

@section('content')
    <p class="breadcrumb"><a href="{{ url('/')}}/">BBeacons</a> &raquo; <a href="{{ url('/rooms')}}/">Rooms</a> &raquo; {{ $room->nameRoom}}</p>
    <div id="wrapper" class="container">

        <section class="main-content">
            <div class="row">
                <div class="span12">
                    <div class="row">
                        <h1 id="roomtitle">{{ $room->nameRoom}}</h1>
                        <div class="span4">
                            <a href="#" class="thumbnail" data-fancybox-group="group1"
                               title="Description 1"><img alt="" src="<?php echo asset('img/rooms/'.$room->id.'.jpg')?>"></a>

                        </div>
                        <div class="span6">
                            <dl class="dl-horizontal">
                                <dt>size:</dt>
                                <dd>{{ $room->nameRoom }}</dd>
                                <dt>Percentage:</dt>
                                <dd>{{ $room->nameRoom }}%</dd><br>
                                <dt> {{count($beacons)}} beacons in this room:</dt>

                                        @foreach ($beacons as $beacon)
                                            <dd>{{$beacon->name}}</dd>
                                        @endforeach
                            </dl>
                        </div>

                    </div>
                    <div class="row">
                        <div class="span12">
                            <ul class="nav nav-tabs" id="myTab">
                                <li class="active"><a href="#home">Description</a></li>
                            </ul>
                            <div class="tab-content">
                                <div class="tab-pane active" id="home">{{ $room->nameRoom}}
                                </div>
                                <div class="tab-pane" id="profile">
                                    <table class="table table-striped shop_attributes">
                                        <tbody>
                                        <tr class="">
                                            <th>Size</th>
                                            <td>Large, Medium, Small, X-Large</td>
                                        </tr>
                                        <tr class="alt">
                                            <th>Colour</th>
                                            <td>Orange, Yellow</td>
                                        </tr>
                                        </tbody>
                                    </table>
                                </div>
                            </div>

                            <p id="back"><a href="/rooms"> &larr; Back to all the rooms</a></p>
                        </div>

                    </div>
                </div>

            </div>
        </section>


    </div>
@endsection