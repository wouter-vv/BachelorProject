{{-- extend the parent tpl --}}
@extends('layouts.master')
{{-- set the pagetitle in the parent tpl --}}
@section('title', 'BBeacons &raquo; room')

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
                                <dt>length:</dt>
                                <dd>{{ $room->length }}</dd>
                                <dt>width:</dt>
                                <dd>{{ $room->width }}</dd><br>
                                <dt>description:</dt>
                                <dd>{{ $room->description }}</dd><br>
                                <dt> {{count($beacons)}} beacons in this room:</dt>

                                        @foreach ($beacons as $beacon)
                                            <dd>{{$beacon->name}}</dd>
                                        @endforeach
                            </dl>
                        </div>

                    </div>
                    <div class="row">
                        <div class="span12 ">
                            <ul class="nav nav-tabs" id="myTab">
                                <li class="active"><a href="#home">Values</a></li>

                            </ul>
                            <p id="back"><a href="/rooms"> &larr; Back to all the rooms</a></p>
                            <div class="tab-content">
                                <table class="table table-striped span11">
                                    <thead>
                                    @if($mvalues)
                                        @forelse($mvalues as $mvalue)
                                            <tr>
                                                <th >{{ $mvalue->moment }}</th>
                                                <th >{{ $mvalue->valueBeacon1 }}</th>
                                                <th >{{ $mvalue->valueBeacon2 }}</th>
                                                <th >{{ $mvalue->valueBeacon3 }}</th>
                                                <th >{{ $mvalue->valueBeacon4 }}</th>
                                            </tr>
                                        @empty
                                            <div class="alert alert-warning col-sm-12" role="alert">
                                                <p>You have no values yet!</p>
                                            </div>
                                        @endforelse
                                    @endif
                                    </thead>

                                </table>

                                </div>
                            <div>
                                <div id="pagination">{{ $mvalues->links () }}</div>

                            </div>


                        </div>

                    </div>
                </div>

            </div>
        </section>


    </div>
@endsection