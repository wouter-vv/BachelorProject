
<?php 
require "connection.php";
$nameRoom = $_POST["nameRoom"];
$width = (int)$_POST["width"];
$length = (int)$_POST["length"];
$description = $_POST["description"];
$mysql_qry = "insert into Rooms (nameRoom, width, length, description) values ('$nameRoom','$width','$length','$description')";
if($conn -> query($mysql_qry) === TRUE ) {
echo "InsertRoomSuccessfull";
}
else {
echo "Error: " . $mysql_qry . "<br>" . $conn->error;
}
$conn->close();
?>
    