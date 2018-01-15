
<?php 
require "connection.php";
$mysql_qry = "TRUNCATE TABLE Values;";
mysqli_query($conn ,$mysql_qry);
?>