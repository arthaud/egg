package mg.egg.eggc.runtime.libjava;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Utils {
	public static char[] getInputStreamAsCharArray(InputStream stream,
			int length, String encoding) throws IOException {
		InputStreamReader reader = null;
		reader = encoding == null ? new InputStreamReader(stream)
				: new InputStreamReader(stream, encoding);
		char[] contents;
		if (length == -1) {
			contents = new char[0];
			int contentsLength = 0;
			int amountRead = -1;
			do {
				int amountRequested = Math.max(stream.available(), 8192);
				// resize contents if needed
				if (contentsLength + amountRequested > contents.length) {
					System.arraycopy(contents, 0,
							contents = new char[contentsLength
									+ amountRequested], 0, contentsLength);
				}

				// read as many chars as possible
				amountRead = reader.read(contents, contentsLength,
						amountRequested);

				if (amountRead > 0) {
					// remember length of contents
					contentsLength += amountRead;
				}
			} while (amountRead != -1);

			// Do not keep first character for UTF-8 BOM encoding
			int start = 0;
			if (contentsLength > 0 && "UTF-8".equals(encoding)) { //$NON-NLS-1$
				if (contents[0] == 0xFEFF) { // if BOM char then skip
					contentsLength--;
					start = 1;
				}
			}
			// resize contents if necessary
			if (contentsLength < contents.length) {
				System.arraycopy(contents, start,
						contents = new char[contentsLength], 0, contentsLength);
			}
		} else {
			contents = new char[length];
			int len = 0;
			int readSize = 0;
			while ((readSize != -1) && (len != length)) {
				// See PR 1FMS89U
				// We record first the read size. In this case len is the actual
				// read size.
				len += readSize;
				readSize = reader.read(contents, len, length - len);
			}
			// Do not keep first character for UTF-8 BOM encoding
			int start = 0;
			if (length > 0 && "UTF-8".equals(encoding)) { //$NON-NLS-1$
				if (contents[0] == 0xFEFF) { // if BOM char then skip
					len--;
					start = 1;
				}
			}

			if (len != length)
				System.arraycopy(contents, start, (contents = new char[len]),
						0, len);
		}

		reader.close();
		return contents;
	}
}
