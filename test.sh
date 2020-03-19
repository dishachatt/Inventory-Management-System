echo -e "\nAdding with data name1 and description1\n"
curl -d "name=name1&description=description1" http://localhost:8080/ims/add 2>/dev/null
echo -e "\nAdding with data name2 and description2\n"
curl -d "name=name2&description=description2" http://localhost:8080/ims/add 2>/dev/null
echo -e "\nAdded two items...\n"
curl http://localhost:8080/ims/details/0 2>/dev/null
curl http://localhost:8080/ims/details/1 2>/dev/null
echo -e "\nRemoving item with ID 1\n"
curl -X DELETE http://localhost:8080/ims/remove/1 2>/dev/null
echo -e "\nItem removed...\n" 
curl http://localhost:8080/ims/details/1 2>/dev/null
echo -e "\nAdding with data name2 and description2\n"
curl -d "name=name2&description=description2" http://localhost:8080/ims/add 2>/dev/null
echo -e "\nAdding with data name3 and description3\n"
curl -d "name=name3&description=description3" http://localhost:8080/ims/add 2>/dev/null
echo -e "\nItems added today: "
curl -d "start=2020-03-18&end=2020-03-21"  http://localhost:8080/ims/log 2>/dev/null
echo -e "\nDone..."