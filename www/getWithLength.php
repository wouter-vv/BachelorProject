<?php 
require "connection.php";
$nameRoom = $_POST["nameRoom"];
$mysql_qry = "select width, length from Rooms where nameRoom like '$nameRoom'";
$result = mysqli_query($conn ,$mysql_qry);
if(mysqli_num_rows($result) > 0) {
    $wth = "WithLength: ";
    while($row = $result->fetch_assoc()) {
        $wth .= $row["width"] . " " . $row["length"];
        
    }
    echo $wth;
}
else {
echo "RoomValuesNot";
}
 
?>