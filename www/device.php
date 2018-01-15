<?php 
require "connection.php";
$Name = $_POST["Name"];
$Room = $_POST["Room"];
$mysql_qry = "insert into Devices (name, Rooms_idRoom) values ('$Name', (select idRoom from Rooms where nameRoom like '$Room' ))";
if($conn -> query($mysql_qry) === TRUE ) {
echo "InsertDeviceSuccesfull";
}
else {
echo "Error: " . $mysql_qry . "<br>" . $conn->error;
}
$conn->close();
?>


