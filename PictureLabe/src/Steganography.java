import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
public class Steganography
{
	
	/*
	 * clear the lower rightmost bits in a pixel
	 */

	public static void clearLow(Pixel p)
	{
		int r = p.getRed();
		int g = p.getGreen();
		int b = p.getBlue();
		
		p.setRed((r/4)*4);
		p.setGreen((g/4)*4);
		p.setBlue((b/4)*4);

	}
	
	
	/*
	 * 
	 * 
	 */
	private static Picture testClearLow(Picture original)
	{
		Picture modified = new Picture(original);
		int h = original.getHeight();
		int w = original.getWidth();
		
		for(int i=0; i<w;i++)
		{
			for(int j = 0; j<h;j++)
			{
				clearLow(modified.getPixel(i, j));

			}
		}
		
		return modified;
		
		
	}
	
	// testing set low method
	public static Picture testSetLow(Picture p, Color clr)
    {
		Picture newPic = new Picture(p);
        Pixel[][] picturePixArray = newPic.getPixels2D();
        for (int r=0; r<picturePixArray.length;r++)
        {
            for (int c = 0; c<picturePixArray[0].length;c++)
            {
                
                setLow(picturePixArray[r][c], clr);
            }
        }

        return newPic;
    }
	
	/*
	 * The lower two bits in a pixel to the highest two bits in c
	 */
	public static void setLow(Pixel p, Color c)
	{
		clearLow(p);
		int rAdd = c.getRed() / 64;
		int baseR = p.getRed();
		int gAdd = c.getGreen()/64;
		int baseG = p.getGreen();
		int bAdd = c.getBlue()/64;
		int baseB = p.getBlue();
		
		Color newColor = new Color(baseR+rAdd,baseG+gAdd, baseB+bAdd);
		p.setColor(newColor);

		
	}
	
	
	/**
	 * Sets the highest two bits of each pixel’s colors
	 * to the lowest two bits of each pixel’s color o s
	*/ 
	public static Picture revealPicture(Picture hidden)
	{
		Picture copy = new Picture(hidden);
		Pixel[][] pixels = copy.getPixels2D();
		Pixel[][] source = hidden.getPixels2D();
		for (int r = 0; r < pixels.length; r++) 
		{
			for (int c = 0; c < pixels[0].length; c++) 
			{
				Color col = source[r][c].getColor();
				int hidR = (col.getRed() % 4) * 64;
				int hidG = (col.getGreen() % 4) * 64;
				int hidB = (col.getBlue() % 4) * 64;
				pixels[r][c].setColor(new Color(hidR, hidG, hidB));

			}
			
		}
		return copy;
	}
	
	
	/**
	 * Creates a new Picture with data from secret hidden in data from source
	 * @param source is not null
	 * @param secret is not null
	 * @return combined Picture with secret hidden in source
	 * precondition: source is same width and height as secret
	 */
	public static Picture hidePicture(Picture source, Picture secret) 
	{
        Pixel[][] secretPixels = secret.getPixels2D();
        
        Picture returnPicture = new Picture(source);
        Pixel[][] returnPixels = returnPicture.getPixels2D();

        for (int r = 0; r < secretPixels.length; r++) {
            for (int c = 0; c < secretPixels[0].length; c++) {
                Pixel secretPixel = secretPixels[r][c];
                Pixel sourcePixel = returnPixels[r][c];
                clearLow(sourcePixel);
                setLow(sourcePixel, secretPixel.getColor());
            }
        }

        return returnPicture;
    }
	
	public static Picture hidePicture2(Picture source, Picture secret, int startCol,int startRow) {
        Picture copy = new Picture(source);
        Pixel[][] pixels = copy.getPixels2D();
        Pixel[][] second = secret.getPixels2D();
        
        for(int r = startRow; r<startRow + second[0].length;r++)
        {
        	for(int c = startCol; c<startCol + second[0].length;c++)
            {
        		Color col = second[r-startRow][c-startCol].getColor();
        		clearLow(pixels[r][c]);
        		setLow(pixels[r][c],col);
            	
            }
   
        	
        }
        return copy;
        
        
    }
	/*
	 *  Takes two pictures and checks to see if the two
pictures are exactly the same (returns true or false).
	 */
	public static boolean isSame(Picture one, Picture two) {
        boolean isHeight = one.getHeight() == two.getHeight();
        boolean isWidth = one.getWidth() == two.getWidth();

        if (isWidth && isHeight) {
            int originalHeigth = one.getHeight();
            int originalWidth = one.getWidth();

            for (int r = 0; r < originalWidth; r++) {
                for (int c = 0; c < originalHeigth; c++) {
                    Pixel pic1 = one.getPixel(r, c);
                    Pixel pic2 = two.getPixel(r, c);
                    if (!(pic1.getRed() == pic2.getRed() && pic1.getBlue() == pic2.getBlue() && pic1.getGreen() == pic2.getGreen())) {
                        return false;
                    }
                }
            }
        } else {
            return false;
        }

        return true;
    }
	
	/**
	 * Takes two pictures and makes a list of the coordinates that are different between them (returns an ArrayList of
points/coordinates).
	 */
	public static ArrayList<Point> findDiffrence(Picture one, Picture two)
	{
		ArrayList<Point> points = new ArrayList<Point>();
		Pixel[][] pix1 = one.getPixels2D();
		Pixel[][] pix2 = two.getPixels2D();
		
		if(one.getHeight() != two.getHeight() || one.getWidth() != two.getWidth())
		{
			return points;
		}

		for(int i = 0; i<pix1.length;i++)
		{
			for(int j = 0; j<pix1[0].length;j++)
			{
				if(pix1[i][j].getRed() != pix2[i][j].getRed() || pix1[i][j].getBlue() != pix2[i][j].getBlue()
						|| pix1[i][j].getGreen() != pix2[i][j].getGreen())
				{
					points.add(new Point(i,j));
				}
			}
		}
		return points;
		
	}
	
	/**
	 * Determines whether secret can be hidden in source, which is
	 * true if source and secret are the same dimensions.
	 * @param source is not null
	 * @param secret is not null
	 * @return true if secret can be hidden in source, false otherwise.
	 */
	public static boolean canHide(Picture source, Picture secret)
	{
		if(source.getHeight()>=secret.getHeight() && source.getWidth()>=secret.getWidth())
		{
			return true;
		}
		return false;
		
	}
	
	
	
	
	
	public static Picture showDifferentArea(Picture source, Picture secret)
	{
		int minx = Integer.MAX_VALUE;
		int miny = Integer.MAX_VALUE;
		int maxx = Integer.MIN_VALUE;
		int maxy = Integer.MIN_VALUE;
		Picture returnPic = new Picture(source);
		ArrayList<Point> points = findDiffrence(source, secret);
		for(Point p:points)
		{
			//check x and y cordinaate greater than maxvalue or less than min value.
			if (p.getX() > maxx)
                maxx = (int) p.getX();
			if(p.getY() > maxy)
				maxy = (int)p.getY();
			if(p.getX() < minx)
				minx = (int)p.getX();
			if(p.getY() < miny)
				miny = (int)p.getY();		
			
		}
		
		for (int i=minx; i<maxx; i++)
        {
            returnPic.getPixel(miny, i).setColor(Color.red);
            returnPic.getPixel(maxy, i).setColor(Color.red);
        }

        for (int i=miny; i<maxy; i++)
        {
            returnPic.getPixel(i, minx).setColor(Color.red);
            returnPic.getPixel(i, maxx).setColor(Color.red);
        }

		
		return returnPic;
		
		
	}
	
	
	
	
	
	
	
	
	/**
	 * Takes a string consisting of letters and spaces and
	* encodes the string into an arraylist of integers.
	 * The integers are 1-26 for A-Z, 27 for space, and 0 for end of
	* string. The arraylist of integers is returned.
	 * @param s string consisting of letters and spaces
	 * @return ArrayList containing integer encoding of uppercase
	* version of s
	 */
	public static ArrayList<Integer> encodeString(String s) 
    { 
        s = s.toUpperCase(); 
        String alpha = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        ArrayList<Integer> result = new ArrayList<Integer>();
        for (int i = 0; i < s.length(); i++) 
        { 
            if (s.substring(i,i+1).equals(" ")) 
            { 
                result.add(27);
            } 
            else 
            {
                result.add(alpha.indexOf(s.substring(i,i+1))+1);
            } 
        }
        result.add(0);
        return result; 
    }
    
    /** * Returns the string represented by the codes arraylist t. 
     * * 1-26 = A-Z, 27 = space 
     * * @param codes encoded strin ng 
     * * @return decoded string 
    */ 
    public static String decodeString(ArrayList<Integer> codes) 
    { 
        String result=""; 
        String alpha = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"; 
        for (int i=0; i < codes.size(); i++) 
        { 
            if (codes.get(i) == 27) 
            { 
                result = result + " ";
            }
            else 
            {
                result = result + alpha.substring(codes.get(i)-1, codes.get(i)); 
            }
        }
        return result; 
    } 
    
    /** * Given a number from 0 to 63, creates and returns a 3- -element 
     * * int array consisting of the integers repre esenting the 
     * * pairs of bits in the number from right to left. 
     * * @param num number to be broken up 
     * * @return bit p pairs in number 
     * */ 
    private static int[] getBitPairs(int num) 
    {
        int[] bits = new int[3];
        int code = num;
        for (int i = 0; i < 3; i++)
        {
            bits[i] = code % 4;
            code = code / 4;
        }

        return bits; 
    } 

    /*** Hide a string (must be only capital letters and space es) in a 
     * *  picture. 
     * *  The string always starts in the upper left corner. 
     * *  @param source picture to hide strin ng in
     * * @param string to hide
     * * @return picture with hid dden string
     * */ 
    public static Picture hideText(Picture source, String s)
    {
        Pixel[][] pixels = source.getPixels2D();

        ArrayList<Integer> nums = encodeString(s);

        for(int c=0; c < nums.size(); c++)
        {
            int[] bits = getBitPairs(nums.get(c)); // for each charater in message we get the pairs
            clearLow(pixels[0][c]); // prepare color for inserting message

            pixels[0][c].setRed(pixels[0][c].getRed() + bits[0]);
            pixels[0][c].setGreen(pixels[0][c].getGreen() + bits[1]);
            pixels[0][c].setBlue(pixels[0][c].getBlue() + bits[2]);
        }

        return source;
    }

    /*** Returns a string hidden in the picture
     * * @param sourc ce picture with hidden string
     * * @return revealed string
     */ 
    public static String revealText(Picture source)
    {
        Pixel[][] pixels = source.getPixels2D();
        ArrayList<Integer>codes = new ArrayList<Integer>();

        int numFound = -1;
        int col = 0;
        while(numFound != 0)
        {
            // add bits
            numFound = pixels[0][col].getColor().getRed()%4;
            numFound += pixels[0][col].getColor().getGreen()%4*4;
            numFound += pixels[0][col].getColor().getBlue()%4*16;

            // one case if message is ended
            if(numFound != 0)
            {
                codes.add(numFound);
            }

            col++; //move to next column
        }
        
        return decodeString(codes);
    }
	public static String cipherIncode(String strIn, int delta) {
        String strOut = "";

        // bring delta to positive value
        while (delta < 0) {
            delta += 26;
        }

        for (int i = 0; i < strIn.length(); i++) {
            char c = strIn.charAt(i);

            if (Character.isAlphabetic(c)) {
                char startLetter = Character.isUpperCase(c) ? 'A' : 'a';    // convert [a, z] to [0, 25]
                strOut += (char) ((c - startLetter + delta) % 26 + startLetter);
            } else {
                strOut += c;
            }
        }

        return strOut;
    }

	public static String cipherDecode(String strIn, int delta) {
	        return cipherIncode(strIn, -delta);
	    }
	
	
	
	
	
	
	public static void main(String args[])
    {
        //Picture beach = new Picture ("beach.jpg"); 
        //beach.explore(); 
        //Picture copy = testSetLow(beach, Color.PINK); 
        //copy.explore(); 
        //Picture reveal = revealPicture(copy);
        //reveal.explore(); 
		
//		Picture beach = new Picture ("beach.jpg"); 
//		Picture arch = new Picture ("arch.jpg"); 
//		if(canHide(beach,arch)==true)
//		{
//			Picture secret = hidePicture(beach,arch);
//			secret.explore();
//			Picture secret2 = revealPicture(secret);
//			secret2.explore();
//		}
//		
//		Picture hall = new Picture("femaleLionAndHall.jpg");
//        Picture robot = new Picture("robot.jpg");
//        Picture flower = new Picture("flower1.jpg");
//        //hide pictures
//        Picture hall1 = hidePicture2(hall, robot, 50, 300);
//        Picture hall2 = hidePicture2(hall1, flower, 115, 275);
//        if (!isSame(hall, hall2)) 
//        {
//            Picture hall4 = showDifferentArea(hall, hall2);
//            hall4.show();
//            Picture unhiddenHall3 = revealPicture(hall2);
//            unhiddenHall3.show();
//        }
        
        
		Picture beach = new Picture ("beach.jpg"); 
        String message = "AMITVA PAL";
        beach.explore();
        Picture hidden = hideText(beach,message);
        hidden.explore();
        System.out.println(revealText(beach));
        
        //lab 5
        
//        beach.explore();
//        hideText(beach, cipherIncode(message, 6));
//        beach.explore();
//        
//        System.out.println("Secret msg revealed (lab 5): " + revealText(beach));
        
        
			
		
		
		
    }
	
	
	
	
	
	
	
	

}
