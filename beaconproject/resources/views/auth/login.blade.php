{{-- extend the parent tpl --}}
@extends('layouts.master')
{{-- set the pagetitle in the parent tpl --}}
@section('title', 'BBeacons &raquo; Login')

@section('content')
    <div class="container">
        <div class="row">
            <div class="span5" id="loginform">
                <div class="panel panel-default">
                    <div id="login_box_header">
                        <h2>
                            Login
                        </h2>
                    </div>
                    <div class="panel-body">
                        <div id="login_box_content">
                            @if (count($errors))
                                <ul class="alert alert-danger">
                                    @foreach ($errors->all() as $error)
                                        <li>
                                            {{ $error }}
                                        </li>
                                    @endforeach
                                </ul>
                            @endif


                            {!! Form::open(['url' => '/login', 'method' => 'POST']) !!}
                            <div class="form-group">
                                <label>
                                    Email
                                </label>
                                {!! Form::email('email', null, ['class' => 'form-control', 'id' => 'name_filter', 'placeholder' => 'Email']) !!}
                            </div>

                            <div class="form-group">
                                <label>
                                    Password
                                </label>
                                {!! Form::password('password', ['class' => 'form-control', 'id' => 'name_filter']) !!}
                            </div>

                            <button type="submit" id="login_button" class="btn btn-default">Login</button>
                            {!! Form::close() !!}
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
@endsection
