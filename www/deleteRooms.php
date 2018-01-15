
<?php 
require "connection.php";
$mysql_qry = "TRUNCATE TABLE Rooms;";
mysqli_query($conn ,$mysql_qry);
?>