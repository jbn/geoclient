/*
 * Copyright 2013-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package gov.nyc.doitt.gis.geoclient.parser.regex;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import gov.nyc.doitt.gis.geoclient.parser.AbstractSpecTest;
import gov.nyc.doitt.gis.geoclient.parser.Input;
import gov.nyc.doitt.gis.geoclient.parser.ParseContext;
import gov.nyc.doitt.gis.geoclient.parser.regex.CountryParser;
import gov.nyc.doitt.gis.geoclient.parser.token.Chunk;
import gov.nyc.doitt.gis.geoclient.parser.token.ChunkType;
import gov.nyc.doitt.gis.geoclient.parser.token.Token;
import gov.nyc.doitt.gis.geoclient.parser.token.TokenType;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CountryParserTest extends AbstractSpecTest
{
	private static final Logger LOGGER = LoggerFactory.getLogger(CountryParserTest.class);
	private CountryParser parser;

	@Before
	public void setUp() throws Exception
	{
		parser = new CountryParser();
	}
	
	@Test
	public void testTokens()
	{
		testParser(parser, LOGGER);
	}
	
	@Test
	public void testParseSuccessChangesCurrentChunk()
	{
		String originalText = "280 Riverside Dr NY NY 10025 U.S.A.";
		ParseContext context = new ParseContext(new Input("junit-test", originalText));
		Chunk initialChunk = context.getCurrent();
		assertThat(initialChunk.getType(), is(equalTo(ChunkType.ORIGINAL_INPUT)));
		parser.parse(context);
		assertThat("ParseContext.isParsed should be",context.isParsed(),is(false));
		assertThat("Chunk.Type of initial Chunk:",initialChunk.getType(), is(equalTo(ChunkType.COUNTY)));
		assertThat("Initial Chunk contains:",initialChunk.contains(new Token(TokenType.COUNTRY, "U.S.A.", 29, 35)), is(true));		
		assertThat("Initial Chunk token count:", initialChunk.tokenCount(),is(1));
		Chunk actualChunk = context.getCurrent();
		assertThat(actualChunk.getType(), is(equalTo(ChunkType.SUBSTRING)));
		assertThat(actualChunk.getText(),is(equalTo("280 Riverside Dr NY NY 10025")));
	}

}
