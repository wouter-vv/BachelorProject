
<?php 
require "connection.php";
$nameRoom = $_POST["nameRoom"];
$mysql_qry = "insert into Rooms (nameRoom) values ('$nameRoom')";
if($conn -> query($mysql_qry) === TRUE ) {
echo "InsertRoomSuccessfull";
}
else {
echo "Error: " . $mysql_qry . "<br>" . $conn->error;
}
$conn->close();
?>
    