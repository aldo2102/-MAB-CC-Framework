#!/bin/bash

value=`cat /home/$USER/falha.csv`
#echo "$value"
IFS=', ' read -r -a array <<< "$value"
if [ "${array[${!values[@]}-1]}" == "right" ]; then
        echo "Tudo ok"
else
        size=${array[${!values[@]}-1]}
        echo "Tudo não ok - Continuando execução"
        java -jar /home/$USER/Untitled.jar "$((${array[0]}-$size))" "$((${array[1]}+$size*${array[2]}))" ${array[2]}
fi

echo "1" > ~/aldo.txt

exit 0

	