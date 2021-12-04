Name:        Rodrigue, Andrew
Project:     PA-2 (RAID)

Description:
    This program uses 3 static methods in the main class to simulate the behavior of disks in a RAID-5 scheme.
    The first method writes the data from an input file to a specified number of disks with a specified block size. There
    must be at least 3 disks to support a RAID-5 scheme. The second method reads the data from the disks into an output
    file. I had some initial confusion on whether the output file would contain a stream showing the actual integer values
    of the bytes of the disks or the character representations of this byte stream. Dr. Chen clarified that he wanted to
    see the actual numeric bytes in this file. The last method will rebuild a "lost" disk by xor'ing the other
    disks in the RAID-5 scheme. I would recommend deleting the file containing the disk contents to simulate actually
    losing a disk. Otherwise, you will not see that this method is behaving as expected, and it will appear as though this
    file is not changing at all.

To run code:
    1. Verify JDK is installed
        java -version

    2. Navigate to prog2
        cd ~/prog2

    3. Compile Main.java
        javac Main.java

    Please follow the following commands in the exact order as they appear.
    This implementation operates under the assumption that the method calls will be called in the correct order and that
    first 2 paramters, # of disks and block size, remain the same when working with the same raid contents. This assumption
    was discussed in class. Make sure you use the exact file name and do not include extensions unless they are included
    in the file name. Also do not forget to include ./ before the file name!!!

    4. Read input file
        java Main <# of disks> <block size> read ./<input file>
        EXAMPLE - java Main 4 16 read ./input

    5. Write raid contents to output file
        java Main <# of disks> <block size> write ./<output file>
        EXAMPLE - java Main 4 16 write ./output

    6. Rebuild a "lost" disk
        java Main <# of disks> <block size> rebuild ./<lost disk file>
        EXAMPLE - java Main 4 16 rebuild ./disk.0

Expected input file:
    - some txt file containing a string of characters or bytes
    - for example, the provided input is shown below

    // sample input (shown on separate lines to show parititions of blocks)
    AAAAAAAAAAAAAAAA
    BBBBBBBBBBBBBBBB
    CCCCCCCCCCCCCCCC
    DDDDDDDDDDDDDDDD
    EEEEEEEEEEEEEEEE
    FFFFFFFFFFFFFFFF
    GGGGGGGGGGGGGGGG
    HHHHHHHHHHHHHHHH
    IIIIIIIIIIIIIIII
    JJJJJJJJJJJJJJJJ
    KKKKKKKKKKKKKKKK
    LLLLLLLLLLLLLLLL
    MMMMMMMMMMMMMMMM
    NNNNNNNNNNNNNNNN
    OOOOOOOOOOOOOOOO
    PPPPPPPPPPPPPPPP
    QQQQQQQQQQQQQQQQ
    RRRRRRRRRRRRRRRR
    SSSSSSSSSSSSSSSS
    TTTTTTTTTTTTTTTT
    UUUUUUUUUUUUUUUU
    VVVVVVVVVVVVVVVV
    WWWWWWWWWWWWWWWW
    XXXXXXXXXXXXXXXX
    YYYYYYYYYYYYYYYY
    ZZZZZZZZZZZZZZZZ
    //

Expected output using input above with # of disks = 4 and block size = 16:
    WRITE EXAMPLE -
        console output:
            andyje@Andrews-MacBook-Pro src % java Main 4 16 write ./input
            Reading contents of ./input...
            disk.0 successfully created.
            disk.1 successfully created.
            disk.2 successfully created.
            disk.3 successfully created.
            Writing contents of file and parity calculations to disks...
            Closing writers...
            Finished write operation!

        disk contents (parity blocks shown in parantheses):
        disk.0                  disk.1                  disk.2                  disk.3
        (pppppppppppppppp)      AAAAAAAAAAAAAAAA        BBBBBBBBBBBBBBBB        CCCCCCCCCCCCCCCC
        DDDDDDDDDDDDDDDD        (wwwwwwwwwwwwwwww)      EEEEEEEEEEEEEEEE        FFFFFFFFFFFFFFFF
        GGGGGGGGGGGGGGGG        HHHHHHHHHHHHHHHH        (vvvvvvvvvvvvvvvv)      IIIIIIIIIIIIIIII
        JJJJJJJJJJJJJJJJ        KKKKKKKKKKKKKKKK        LLLLLLLLLLLLLLLL        (}}}}}}}}}}}}}}}})
        (||||||||||||||||)      MMMMMMMMMMMMMMMM        NNNNNNNNNNNNNNNN        OOOOOOOOOOOOOOOO
        PPPPPPPPPPPPPPPP        (cccccccccccccccc)      QQQQQQQQQQQQQQQQ        RRRRRRRRRRRRRRRR
        SSSSSSSSSSSSSSSS        TTTTTTTTTTTTTTTT        (bbbbbbbbbbbbbbbb)      UUUUUUUUUUUUUUUU
        VVVVVVVVVVVVVVVV        WWWWWWWWWWWWWWWW        XXXXXXXXXXXXXXXX        (iiiiiiiiiiiiiiii)
        () YYYYYYYYYYYYYYYY        ZZZZZZZZZZZZZZZZ         0000000000000000

        NOTICE that the parity striping starts at disk 0 not from the end

    READ EXAMPLE -
        console output:
            andyje@Andrews-MacBook-Pro src % java Main 4 16 read ./output
            Creating ./output to store raid contents...
            Creating reader for disk.0 ...
            Creating reader for disk.1 ...
            Creating reader for disk.2 ...
            Creating reader for disk.3 ...
            Writing raid contents...
            Closing writer and readers...
            Finished read operation!


        output contents (shown in bytes and on separate lines for clarity):
            112112112112112112112112112112112112112112112112
            65656565656565656565656565656565
            66666666666666666666666666666666
            67676767676767676767676767676767
            68686868686868686868686868686868
            119119119119119119119119119119119119119119119119
            69696969696969696969696969696969
            70707070707070707070707070707070
            71717171717171717171717171717171
            72727272727272727272727272727272
            118118118118118118118118118118118118118118118118
            73737373737373737373737373737373
            74747474747474747474747474747474
            75757575757575757575757575757575
            76767676767676767676767676767676
            125125125125125125125125125125125125125125125125
            124124124124124124124124124124124124124124124124
            77777777777777777777777777777777
            78787878787878787878787878787878
            79797979797979797979797979797979
            80808080808080808080808080808080
            99999999999999999999999999999999
            81818181818181818181818181818181
            82828282828282828282828282828282
            83838383838383838383838383838383
            84848484848484848484848484848484
            98989898989898989898989898989898
            85858585858585858585858585858585
            86868686868686868686868686868686
            87878787878787878787878787878787
            88888888888888888888888888888888
            105105105105105105105105105105105105105105105105
            3333333333333333
            89898989898989898989898989898989
            90909090909090909090909090909090
            48484848484848484848484848484848

    REBUILD
        console output:
            andyje@Andrews-MacBook-Pro src % java Main 4 16 rebuild ./disk.0
            Simulating "losing" ./disk.0 ...
            Creating reader for disk.1 ...
            Creating reader for disk.2 ...
            Creating reader for disk.3 ...
            Recovering lost data through xor operation...
            Writing data to new ./disk.0 ...
            Closing writer and readers...
            Finished rebuild operation!