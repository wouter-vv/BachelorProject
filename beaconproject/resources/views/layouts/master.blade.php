<!DOCTYPE html>
<!--[if lt IE 7 ]><html class="oldie ie6" lang="en"><![endif]-->
<!--[if IE 7 ]><html class="oldie ie7" lang="en"><![endif]-->
<!--[if IE 8 ]><html class="oldie ie8" lang="en"><![endif]-->
<!--[if IE 9 ]><html class="ie9" lang="en"><![endif]-->
<!--[if (gt IE 9)|!(IE)]><!--><html lang="en"><!--<![endif]-->
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>@yield('title')</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">

    <!--[if ie]><meta content='IE=8' http-equiv='X-UA-Compatible'/><![endif]-->

    <link rel="stylesheet" media="screen" href="{{ asset('/css/bootstrap.css') }}">
    <link rel="stylesheet" media="screen" href="{{ asset('/css/bootstrap-responsive.css') }}">
    <link rel="stylesheet" media="screen" href="{{ asset('/css/layout.css') }}">

    <link href="{{ asset('/themes/css/bootstrappage.css') }}" rel="stylesheet"/>

    <!-- global styles -->
    <link href="{{ asset('/themes/css/flexslider.css') }}" rel="stylesheet"/>
    <link href="{{ asset('/themes/css/main.css') }}" rel="stylesheet"/>

    <!-- scripts -->
    <script src="{{ asset('/themes/js/jquery-1.7.2.min.js') }}"></script>
    <script src="{{ asset('/js/bootstrap.min.js') }}"></script>
    <script src="{{ asset('/themes/js/superfish.js') }}"></script>
    <script src="{{ asset('/themes/js/jquery.scrolltotop.js') }}"></script>


    <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
    <script type="text/javascript">
      google.charts.load('current', {'packages':['corechart']});
      google.charts.setOnLoadCallback(drawChart);

      function drawChart() {
        var data = google.visualization.arrayToDataTable([
          ['movement', 'Person1', 'Person2'],
          ['15:00',  1,      8],
          ['16:00',  3,      9],
          ['17:00',  5,       2],
          ['18:00',  2,      3]
        ]);

        var options = {
          title: 'movement in meters',
          curveType: 'function',
          legend: { position: 'bottom' }
        };

        var chart = new google.visualization.LineChart(document.getElementById('curve_chart'));

        chart.draw(data, options);
      }
    </script>

</head>
<body>
<div id="top-bar" class="container">
    <div class="row">
        <div class="span12">

        </div>
    </div>
</div>
<div id="wrapper" class="container">
    <section class="navbar main-menu">
        <div class="navbar-inner main-menu">
            <a href="/" class="logo pull-left"><h1>BBeacons</h1></a>
            <nav id="menu" class="pull-right">
                <ul>
                    <li><a href="/home">Home</a></li>
                    <li><a href="/rooms">Rooms</a></li>
                </ul>
            </nav>
        </div>
    </section>
    <div id="siteWrapper">
        <!-- content -->
        <section role="main">

            @yield('content')

        </section>

    </div>

    <footer class="footer">
        <section id="footer-bar">
            <div class="row">

                <div class="span12" id="footerSentence">
                    <p>&copy; {{ \Carbon\Carbon::now()->year }} &mdash; Wouter Vande Velde, Thomas Van Raemdonck  &mdash; ISEP Erasmusproject</p>
                    <br/>
                </div>
            </div>
        </section>
    </footer>
</div>
</body>
</html>