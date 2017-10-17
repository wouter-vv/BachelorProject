{{-- extend the parent tpl --}}
@extends('layouts.master')
{{-- set the pagetitle in the parent tpl --}}
@section('title', 'BBeacons &raquo; Home')

@section('content')
    <p class="breadcrumb"><a href="{{ url('/')}}/">BBeacons</a> &raquo; Home</p>
    <section class="main-content">
        <div class="row">
            <div class="span12">
                <div class="row">
                    <div class="span12">

                        <div>

                            <ul class="thumbnails">
                                
                                <div id="curve_chart" style="width: 900px; height: 500px"></div>

                            </ul>
                        </div>

                    </div>
                </div>
                <br/>

            </div>
        </div>
    </section>
@endsection