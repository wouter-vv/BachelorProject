<?php 
require "connection.php";
$Devices_UUID = $_POST["Devices_UUID"];
$Rooms_Id = $_POST["Rooms_Id"];
$mysql_qry = "insert into devices (UUID, Name) values ('$UUID', '$Name')";
if($conn -> query($mysql_qry) === TRUE ) {
echo "Insert Device Successfull";
}
else {
echo "Error: " . $mysql_qry . "<br>" . $conn->error;
}
$conn->close();
?>