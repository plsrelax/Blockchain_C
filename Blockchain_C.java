/*
 * Bryce Jensen
 * 10/16/2020
 *
 *  openjdk 11.0.1 2018-10-16 LTS
 *  to compile:
 *      $ javac Blockchain_C.java
 *
 *  to run, in one shell:
 *     $ java Blockchain_C
 *
 *  Files needed to run:
 *                     a. checklist-block.html
 *                     b. Blockchain.java
 *                     c. BlockchainLog.txt
 *                     d. BlockchainLedgerSample.json
 *                     e. BlockInput0.txt
 *                     f. BlockInput1.txt
 *                     g. BlockInput2.txt
 *
 * Notes:
 *       This is mini-project C of the Blockchain assignment.
 *
 *       It contains a simple blockchain program with five nodes. A dummy genesis block and
 *       four other simple blocks.
 *
 *       Each block contains some arbitrary data, the hash of the previous block,
 *       a timestamp of its creation, and the hash of the block itself.
 *
 *       When calculating the hash for each block, the contained elements in the block
 *       are turned into strings and concatenated together with a nonce to then be hashed.
 *
 *       The verifying of blocks is done by taking in the block hash prefix and trying every possible
 *       combination by incrementing our nonce  until our prefixString is equal to our designated prefix
 *		
 *	    It currently can marshall its data out into JSON format and compile successfully.
 *
 *      As of 10/17/2020 at 10:41am, I" have my readFromJSON() method flushed out. Will need to clone
 *      and test in the VM to make sure everything still compiles and does what I expect it to.
 *
 */

import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

class BlockRecord
{
    String BlockID;
    // will hold the blocks ID
    String VerificationProcessID;
    // holds the ID of the process that verifies the block, or tries to
    String PreviousHash;
    // hash of the previous block
    UUID uuid;
    // how we will marshall data to JSON
    String Data;
    // the data contained in the block
    String RandomSeed;
    // this will be our means of trying to verify the block
    String WinningHash;
    // the hash of our winning guess

    public String getBlockID()
    {
        return BlockID;
        // accessor to return block ID
    }
    public void setBlockID(String _BlockID)
    {
        this.BlockID = _BlockID;
        // accessor for setting the block ID
    }

    public String getVerificationProcessID()
    {
        return VerificationProcessID;
        // accessor to return verificationProcessID
    }
    public void setVerificationProcessID(String _VerificationProcessID)
    {
        this.VerificationProcessID = _VerificationProcessID;
    }

    public UUID getUUID()
    {
        return this.uuid;
    }
    public void setUUID(UUID _uuid)
    {
        this.uuid = _uuid;
    }
    // get/setter for unique identifier

    public String getPreviousHash()
    {
        return this.PreviousHash;
    }
    public void setPreviousHash(String _PreviousHash)
    {
        this.PreviousHash = _PreviousHash;
    }
    // getter/setter for previousHash

    public String getData()
    {
        return this.Data;
    }
    public void setData(String _Data)
    {
        this.Data = _Data;
    }
    // getter / setter for obtaining and setting the data contained

    public String getRandomSeed()
    {
        return this.RandomSeed;
    }
    public void setRandomSeed(String _RandomSeed)
    {
        this.RandomSeed = _RandomSeed;
    }
    // getter / setters fro gettting and setting the random seed

    public String getWinningHash()
    {
        return this.WinningHash;
    }
    public void setWinningHash(String _WinningHash)
    {
        this.WinningHash = _WinningHash;
    }
    // getter and setters to obtain or set the winning hash
}


public class Blockchain_C
{
    private String hash;
    private String previousHash;
    private String data;
    private long timeStamp;
    private int  nonce;
    // declaration of private member variables

    /*
     * public constructor for Blockchain_C
     * @param data var of type String
     * @param previousHash var of type String
     * @param timeStamp variable of type long
     */
    public Blockchain_C(String data, String previousHash, long timeStamp)
    {
        this.data = data;
        this.previousHash = previousHash;
        this.timeStamp = timeStamp;
        // getters and setters
        this.hash = calculateBlockHash();
        // assigns hash to itself
    }


    public String calculateBlockHash()
    // method to calculate hash for current block
    {
        String dataToHash = previousHash + Long.toString(timeStamp) + Integer.toString(nonce) + data;
        // concatenation of hash of the previous tx ,time of tx, the tx nonce, ans the tx data
        MessageDigest digest = null;
        // declare new message digest objecgt and isntatntiate to null
        byte[] bytes = null;
        // declare and initialize a new byte array

        try
        {
            digest = MessageDigest.getInstance("SHA-256");
            // get an instance of the SHA256 hashing algorithm and store it in digest
            bytes = digest.digest(dataToHash.getBytes("UTF-8"));
            // generate the hash value of our input data and stick in in our new byte array
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException exception)
        {
            System.err.println("Exception found: " + exception);
            exception.printStackTrace();
            // print exceptions to console
        }

        StringBuffer buffer = new StringBuffer();
        // declare and initialize anew string buffer
        for (byte b: bytes)
        // cycle through all bytes in bytes
        {
            buffer.append(String.format("%02x", b));
            // turn said byte into a hex string
        }
        return buffer.toString();
        // return our string buffer that now holds our hash
    }


    /*
     * method for mining a new block
     * @param a prefix var of type integer
     *
     * please note that this implementation does not verifying any date which
     * is a crucial component of blockchains with real-world application
     */
    public String mineBlock(int prefix)
    {
        String prefixString = new String(new char[prefix]).replace('\0', '0');
        /*
         * declare and intialialize our prefix string to a new string containing our prefix integer with '\0' replaced
         * by '0' to represent the prefix we are looking for
         */

        while (!hash.substring(0, prefix).equals(prefixString))
        // while we do not have our desired solution
        {
            nonce++;
            // increment our nonce
            hash = calculateBlockHash();
            // and calculate the hash
        }
        return hash;
        // return our winning hash w=once we find our desired prefixString
    }

    public String getHash()
    {
        return this.hash;
        // getter to return hash
    }

    public String getPreviousHash()
    {
        return this.previousHash;
        // getter to return previous hash
    }

    public String getData()
    {
        return this.data;
    }

    public void sendData(String data)
    {
        this.data = data;
        // method to send data to the block
    }

    public static void writeToJSON()
    {
        System.out.println("\n___________In writeToJSON___________\n");
        // console header to inform the user whats executing
        UUID b_UUID = UUID.randomUUID();
        String s_uuid = b_UUID.toString();
        // declare and initialize a new random uuid and save it as a string
        System.out.println("Unique Block ID: " + s_uuid + "\n");
        // print out uuid to console

        BlockRecord blockRecord = new BlockRecord();
        // declare and initialize a new blockRecord object
        blockRecord.setVerificationProcessID("Process 2");
        // set the process ID to 2
        blockRecord.setBlockID(s_uuid);
        // set the uuid string
        blockRecord.setUUID(b_UUID);
        // set the binary uuid
        blockRecord.setData("This is the data contained within this transaction block");
        // add in some arbitrary data

        Random rand = new Random();
        // declare3 and initialize a new random variable
        int randVal = rand.nextInt(12777215);
        // the declared bound is a 0xFFFFFF mask, Elliott wants us to pick anew range so play around with this

        String randomSeed = String.format("%06X", randVal & 0x00FFFFF);
        // Masking off ll but the trailing 12 characters
        randVal = rand.nextInt(14333409);
        // this bound is meaningless, I made it up
        String randomSeed2 = Integer.toHexString(randVal);
        // second random seed string
        System.out.println("Our random seed is: " + randomSeed + "... or was it: " + randomSeed2 + "?...\n");
        // print out our two random seeds to confuse the user, we are using randomSeed2

        blockRecord.setRandomSeed(randomSeed2);
        // set the correct random seed in our blockRecord object

        String newBlockRecord = blockRecord.getBlockID() + blockRecord.getVerificationProcessID() +
                blockRecord.getPreviousHash() + blockRecord.getData() + blockRecord.getRandomSeed() +
                blockRecord.getWinningHash();
        // fill a new string up with our block data

        System.out.println("blockRecord is: " + newBlockRecord + "\n");
        // tell the console what the new block record is

        String SHA256string = "";
        // declare new string variable to hold the string version of our SHA256 hash and initialize to empty;

        try {
            MessageDigest ourMD = MessageDigest.getInstance("SHA-256");
            // get and instance of our hashing algorithm from message digest
            ourMD.update(newBlockRecord.getBytes());
            // load up the bytes of our new block record
            byte[] byteArr = ourMD.digest();
            // turn our record into a byte array

            StringBuffer stringBuf = new StringBuffer();
            for (int i = 0; i < byteArr.length; i++)
            {
                stringBuf.append(Integer.toString((byteArr[i] & 0xFF) + 0x100, 16).substring(1));
                // cycle through all bytes in our byte array and add the hexidecimal verion to our string buffer
            }

            SHA256string = stringBuf.toString();
            // more human readable this way
        } catch (NoSuchAlgorithmException noAlgEx)
        {
            System.out.println("No Algorithm exception caught: " + noAlgEx + "\n");
            noAlgEx.printStackTrace();
            // print our exceptions to the console ot be handled
        }

        blockRecord.setWinningHash(SHA256string);
        // we just let the first hash win, try to implement some real work to see how this may work

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        // declare and initialize new gson builder to contain our java object
        String json = gson.toJson(blockRecord);

        System.out.println("\nJSON blockRecord: " + json + "\n");
        // print our json string to console

        try(FileWriter writer = new FileWriter("blockRecord.json"))
        {
            gson.toJson(blockRecord, writer);
        } catch (IOException exception)
        {
            System.out.println("Caught IO exception: " + exception + "\n");
            exception.printStackTrace();
            // print exception to console to be handled
        }
    }

    public static void readFromJSON()
    {
        System.out.println("\n_______________In readFromJSON_______________");
        // crewate a header to indicate to the console what section is executing
        Gson gson = new Gson();
        // declare and initialize a new gson object and store it in var gson
        try (Reader reader = new FileReader("blockRecord.json"))
        {
            BlockRecord blockRecord_Read = gson.fromJson(reader, BlockRecord.class);
            // reads JSON into variable blockRecord_Read object of type BlockRecord
            System.out.println(blockRecord_Read);
            // print out our newly marshalled java object
            System.out.println("Top Secret Data Contained: " + blockRecord_Read.Data + "\n");
            // print the data contained in the read JSON block
            String uuid_read = blockRecord_Read.uuid.toString();
            // declare and initialize var uuid_read to hold the uuid read from the JSON string
            System.out.println("Stirng UUID: " + blockRecord_Read.BlockID + " \nBinary UUID: " + uuid_read + "\n");
            // print our uuid to console in both string format and binary
        } catch (IOException exception)
        {
            System.out.println("Caught an IOException trying to sneak by: " + exception + "\n");
            exception.printStackTrace();
            // print out the caught exceptions to the console to be handled
        }
    }

    public static void main(String a[])
    {
        List<Blockchain_C> blockchain = new ArrayList<>();
        // declare and initialize our new blockchain
        int prefix = 4;
        // declare and initialize our prefix value to 4 leading zeroes
        String prefixString = new String(new char[prefix]).replace('\0', '0');
        // declare and initialize our prefixString for this instance

        Blockchain_C genesisBlock = new Blockchain_C("This is the genesis Dummy Block.", "0", new Date().getTime());
        // declare and initialize a new genesis block to be our Dummy Block
        genesisBlock.mineBlock(prefix);
        // mine our Dummy Block
        blockchain.add(genesisBlock);
        // add it to our blockchain

        Blockchain_C firstBlock = new Blockchain_C("This is the first Simple Block.", genesisBlock.getHash(), new Date().getTime());
        // declare and initialize our first Simple Block
        firstBlock.mineBlock(prefix);
        // mine our first Simple block
        blockchain.add(firstBlock);
        // add it to our blockchain

        Blockchain_C secondBlock = new Blockchain_C("This is the second Simple Block.", firstBlock.getHash(), new Date().getTime());
        // declare and initialize our second Simple Block
        secondBlock.mineBlock(prefix);
        // mine our second Simple block
        blockchain.add(secondBlock);
        // add it to our blockchain

        Blockchain_C thirdBlock = new Blockchain_C("This is the third Simple Block.", secondBlock.getHash(), new Date().getTime());
        // declare and initialize our third Simple Block
        thirdBlock.mineBlock(prefix);
        // mine our third Simple block
        blockchain.add(thirdBlock);
        // add it to our blockchain

        Blockchain_C fourthBlock = new Blockchain_C("This is the fourth Simple Block.", blockchain.get(blockchain.size() - 1).getHash(), new Date().getTime());
        // declare and initialize our fourth Simple Block
        fourthBlock.mineBlock(prefix);
        // mine our fourth Simple block
        blockchain.add(fourthBlock);
        // add it to our blockchain

        boolean flag = true;
        // declare and initialize our boolean flag var to true
        String tempData = null;
        // declare adn initialize a tempData var to null
        String tempCurrentHash = null;
        // declare and initialize a tempCurrentHash var to null

        for (int i = 0; i < blockchain.size(); i++)
        // cycle through the size of the chain
        {
            String previousHash = i==0 ? "0" : blockchain.get(i - 1).getHash();
            flag = blockchain.get(i).getHash().equals(blockchain.get(i).calculateBlockHash()) &&
                    previousHash.equals((blockchain.get(i).getPreviousHash())) && blockchain.get(i).getHash().substring(0, prefix).equals(prefixString);
            /*
             * set flag equal to the boolean value if the stored hash for the current block is calculated and stored correctly
             * and if the previous block stored in the current block is actually the hash of the previous block.
             * and if the current block has been mined
             */

            tempCurrentHash = blockchain.get(i).getHash();
            // save current hash to print out
            tempData = blockchain.get(i).getData();
            // save data to tempData to print out

            if (!flag)
            {
                break;
            }
        }
        System.out.println("\nFlag: " + flag + "\nBlock: " + tempCurrentHash + "\nContent: " + tempData + "\nGood Job!\n");
        // print out the results to the console

        writeToJSON();
        // write our output to JSON file
        readFromJSON();
        // read our input from a JSON file
    }
}



