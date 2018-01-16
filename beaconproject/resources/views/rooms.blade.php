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
                <ul class="thumbnails">
                    <table class="table table-striped">
                        <thead>
                        @if($rooms)
                            @forelse($rooms as $room)
                                <tr>
                                    <th ><a href="/rooms/{{ $room->id }}">{{ $room -> nameRoom }}</a></th>
                                    <td><a href="/rooms/{{ $room->id }}" class="btn btn-primary pull-right"><span class="glyphicon glyphicon-pencil" aria-hidden="true"></span> Details</a></td>
                                </tr>
                            @empty
                                <div class="alert alert-warning col-sm-12" role="alert">
                                    <p>You have no rooms yet!</p>
                                    <a href="#">Add a room &rarr;</a>
                                </div>
                            @endforelse
                        @endif
                        </thead>
                    </table>
                </ul>

            </div>
            <br/>

        </div>
    </section>
@endsection