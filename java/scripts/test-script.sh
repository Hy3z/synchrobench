#!/bin/bash

case $1 in
	"1" )	echo CoarseGrainedListIntSet
			OUTPUT="CoarseGrainedListBasedSet"
	;;
	"2" )	echo HandOverHandListIntSet
			OUTPUT="HandOverHandBasedSet"
	;;
	"3" )	echo LazyLinkedListSortedSet
			OUTPUT="LazyLinkedListSortedSet"
	;;
	*)		echo "Specify algorithm"
			exit 0
esac

echo "Who I am: $OUTPUT on `uname -n`"
echo "started on" `date`

mkdir out

for thread in 1 4 6 8 10 12
do
	for update_ratio in 0 10 100
	do
	  for size in 100 1000 10000
	  do
	    range=$((2*size))
      echo "→ $OUTPUT	$thread	$update_ratio $size $range"
      out=./out/${OUTPUT}.${thread}.${update_ratio}.${size}.log
      # shellcheck disable=SC2046
      # shellcheck disable=SC2211
      java -cp bin contention.benchmark.Test -b linkedlists.lockbased.$OUTPUT -d 2000 -t $thread -u $update_ratio -i $size -r $range -W 0 >> ${out}
      echo "$OUTPUT	$thread	$update_ratio $size $range done"

		done
#		echo "→ $OUTPUT	$i	$j	without -W 0"
#		java -cp bin contention.benchmark.Test -b linkedlists.lockbased.$OUTPUT -d 3000 -t $i -u $j -i 1024 -r 2048 | grep Throughput
	done
	# wait
done
echo "finished on" `date`
echo "DONE \o/"
