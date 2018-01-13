<?php 
require "connection.php";
$UUID = $_POST["UUID"];
$Name = $_POST["Name"];
$mysql_qry = "insert into devices (UUID, Name) values ('$UUID', '$Name')";
if($conn -> query($mysql_qry) === TRUE ) {
echo "Insert Device Successfull";
}
else {
echo "Error: " . $mysql_qry . "<br>" . $conn->error;
}
$conn->close();
?>
    