
<?php 
require "connection.php";
$UUID1 = $_POST["UUID1"];
$UUID2 = $_POST["UUID2"];
$UUID3 = $_POST["UUID3"];
$UUID4 = $_POST["UUID4"];
$mysql_qry = "insert into rooms (UUID1, UUID2, UUID3, UUID4) values ('$UUID1', '$UUID2', '$UUID3', '$UUID4')";
if($conn -> query($mysql_qry) === TRUE ) {
echo "Insert Successfull";
}
else {
echo "Error: " . $mysql_qry . "<br>" . $conn->error;
}
$conn->close();
?>
    