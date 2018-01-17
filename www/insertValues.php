<?php 
require "connection.php";
$nameRoom = $_POST["nameRoom"];
$valueBeacon1 = $_POST["valueBeacon1"];
$valueBeacon2 = $_POST["valueBeacon2"];
$valueBeacon3 = $_POST["valueBeacon3"];
$valueBeacon4 = $_POST["valueBeacon4"];
$mysql_qry = "insert into MeasureValues (Rooms_id, valueBeacon1, valueBeacon2, valueBeacon3, valueBeacon4) values ((select id from Rooms where nameRoom like '$nameRoom'),'$valueBeacon1','$valueBeacon2','$valueBeacon3','$valueBeacon4')";
if($conn -> query($mysql_qry) === TRUE ) {
    echo "InsertValueSuccessfull";
}
else {
    echo "Niet goed";
}
$conn->close();
?>
