package goit.hw11;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static java.util.stream.LongStream.iterate;

public class Main {
    public static void main(String[] args) {
        String [] names = {
                "Fili",
                "Kili",
                "Dwalin",
                "Balin",
                "Bifur",
                "Bofur",
                "Bombur",
                "Dori",
                "Nori",
                "Ori",
                "Oin",
                "Gloin"
        };

        String [] digits = {
                "123, 12, 45,15",
                "35, 34,14",
                "48,13,45,  25"
        };

        System.out.println ( "1. Print only odd names ");
        System.out.println ( new BedlamOdd (  ) .filterOdd (Arrays .stream ( names ) ) );
        System.out.println (  );
        System.out.println ( "2. Reverse sort and uppercase names" );
        System.out.println ( new BedlamUpperSort (  ) .process ( Arrays .stream ( names ) ) );
        System.out.println (  );
        System.out.println ( "3. Parse numbers in string array" );
        System.out.println ( new BedlamParseDigits (  ) .process ( Arrays .stream ( digits ) ) );
        System.out.println (  );
        System.out.println ( "4. Create quazirandom" );
        System.out.println ( new BedlamRandom (  ) .process ( 16, 100 ) );
        System.out.println (  );
        System.out.println ( "5. Zip streams" );
        System.out.println ( new BedlamZip (  ) .process (
                Stream.of ( 0,1,2,3,4,5,6,7 ),
                Stream .of ( 63,62,61,60,59 )
        ) );
        System.out.println ( new BedlamZip (  ) .process (
                Stream.of ( "Ivanov","Rabinovich","to" ),
                Stream .of ( "and","go","Haifa" )
        ) );
    }
}

class BedlamOdd {
    public String filterOdd ( Stream<String> stream ) {
        // Надеюсь, я правильно понял условие
        final Indexator indexator = new Indexator (  );
        return stream
                .map ( p -> String .format ( "%d. %s", indexator .next (  ), p ) )
                .filter ( p -> Integer .parseInt ( p .split ( "\\." ) [0] ) % 2 != 0 )
                .collect ( Collectors .joining ( ", " ) );
    }

    private static class Indexator {
        private int index = 1;
        public int next (  ) {
            return index++;
        }
    }
}

class BedlamUpperSort {
    public List<String> process ( Stream<String> stream ) {
        return stream
                .map ( String::toUpperCase )
                .sorted (Comparator .reverseOrder (  ) )
                .collect ( Collectors .toList (  ) );
    }
}

class BedlamParseDigits {
    public String process ( Stream<String> stream ) {
        return stream
                //  Есть ли разница - делать в одном выражении...
                // .flatMap ( p -> Arrays .stream ( p .split ( ",\\s*" ) ) )
                // ... или в двух?
                .map ( p -> p .split ( ",\\s*" ) )
                .flatMap ( a -> Arrays .stream ( a ) )

                .sorted ( Comparator.comparingInt ( Integer::parseInt ) )
                .collect ( Collectors .joining ( ", " ) );
    }
}

class BedlamRandom {
    public List<Long> process ( int limit, int seed ) {
        return (List<Long>) generator ( 25214903917L, 11L, 2^48, seed )
                .limit ( limit )
                .boxed (  )
                .collect ( Collectors .toList (  ) );
    }

    public LongStream generator ( long a, long c, long m, int seed ) {
        return LongStream.iterate ( seed, p -> ( a * p + c ) % m );
    }
}

class BedlamZip {
    public <T> List<T> process ( Stream<T> stream1, Stream<T> stream2 ) {
        var r = BedlamZip .zip ( stream1, stream2 );
        return r .collect(Collectors.toList());
        //return BedlamZip .zip ( stream1, stream2 )
        //    .collect ( Collectors .toList (  ) );
    }

    public static <T> Stream<T> zip ( Stream<T> first, Stream<T> second ) {
        Stream.Builder<T> streamBuilder = Stream .builder (  );
        var fIt = first .iterator (  );
        var sIt = second .iterator (  );
        while ( fIt .hasNext (  ) && sIt .hasNext (  ) ) {
            streamBuilder .add ( fIt .next (  ) );
            streamBuilder .add ( sIt .next (  ) );
        }
        return streamBuilder .build (  );
    }
}