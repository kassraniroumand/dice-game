import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;


public class Dice {
	private int faceValue;
	private int numFaces;

	public Dice(int numFaces) {
		this.numFaces = numFaces;
	}

	public int roll() {
		int trueRandomNumber;
		int rnd = this.getTrueRandomNumber(1, 6);
		if (rnd != -1 ){
			trueRandomNumber = rnd;
		} else {
			SecureRandom secureRandom = new SecureRandom();
			trueRandomNumber =  secureRandom.nextInt(numFaces) + 1;
		}
		return trueRandomNumber;
	}

	public int getFaceValue() {
		return faceValue;
	}


	public int getTrueRandomNumber(int min, int max) {
		try {
			// Request a true random number from random.org (free API key might be needed)
			String apiUrl = String.format(
					"https://www.random.org/integers/?num=1&min=1&max=6&col=1&base=10&format=plain&rnd=new",
					min, max);
			URL url = new URL(apiUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");

			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String response = in.readLine();
			System.out.println(response);
			in.close();

			return Integer.parseInt(response.trim());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return -1; // Default value in case of an error
	}
}
