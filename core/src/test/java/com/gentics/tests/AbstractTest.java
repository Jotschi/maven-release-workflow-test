package com.gentics.tests;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import org.junit.Test;

public abstract class AbstractTest {

	@Test
	public void testHostInfo() throws IOException, InterruptedException {
		String OS = System.getProperty("os.name").toLowerCase();

		if (OS.indexOf("win") >= 0) {
			System.out.println("Windows computer name throguh env:\"" + System.getenv("COMPUTERNAME") + "\"");
			System.out.println("Windows computer name through exec:\"" + execReadToString("hostname") + "\"");
		} else {
			if (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0) {
				System.out.println("Linux computer name throguh env:\"" + System.getenv("HOSTNAME") + "\"");
				System.out.println("Linux computer name through exec:\"" + execReadToString("hostname") + "\"");
				System.out.println("Linux computer name through /etc/hostname:\"" + execReadToString("cat /etc/hostname") + "\"");
			}
		}
		Thread.sleep(10000);

	}

	@Test
	public void testName1() throws Exception {
		Thread.sleep(10000);
	}

	@Test
	public void testName2() throws Exception {
		Thread.sleep(10000);
	}

	@Test
	public void testName3() throws Exception {
		Thread.sleep(10000);
	}

	public static String execReadToString(String execCommand) throws IOException {
		Process proc = Runtime.getRuntime().exec(execCommand);
		try (InputStream stream = proc.getInputStream()) {
			try (Scanner s = new Scanner(stream).useDelimiter("\\A")) {
				return s.hasNext() ? s.next() : "";
			}
		}
	}
}
