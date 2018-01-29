{{-- extend the parent tpl --}}
@extends('layouts.master')
{{-- set the pagetitle in the parent tpl --}}
@section('title', 'BBeacons &raquo; Rooms')

@section('content')
    <p class="breadcrumb"><a href="{{ url('/')}}/">BBeacons</a> &raquo; Rooms</p>
    <section class="main-content">
        <div class="row">
            <div class="span11">
                <h2 class="title">Rooms</h2>

                <div>
                @if($rooms)
                    <ul id="roomsMain">
                    @forelse($rooms as $room)
                        <li class="span3">
                            <div class="product-box">
                                <a href="/rooms/{{ $room->id }}"></a>
                                <span class="sale_tag"></span>
                                <a href="/rooms/{{ $room->id }}" class="title">{{ $room->nameRoom }}</a><br/>

                            </div>
                        </li>
                    @empty
                        <div class="alert alert-warning col-sm-12" role="alert">
                            <p>You have no rooms yet!</p>
                        </div>
                    @endforelse
                    </ul>
                @endif
                </div>

            </div>
            <br/>

        </div>
    </section>
@endsection