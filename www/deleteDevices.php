
<?php 
require "connection.php";
$mysql_qry = "TRUNCATE TABLE Devices;";
mysqli_query($conn ,$mysql_qry);
?>