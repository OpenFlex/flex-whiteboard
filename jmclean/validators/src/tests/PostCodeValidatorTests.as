package tests
{
	import mx.validators.PostCodeValidator;
	
	import org.flexunit.asserts.assertTrue;

	public class PostCodeValidatorTests
	{		
		public var validator:PostCodeValidator;
		
		[Before]
		public function setUp():void
		{
			validator = new PostCodeValidator();

			// Currently stored in a static variables as the the doValidation method is static
			validator.format = null;
			validator.countryCode = null;
		}
		
		[After]
		public function tearDown():void
		{
			validator = null;
		}

		[Test]
		public function initial():void
		{
			assertTrue("Format is null", validator.format == null);
			assertTrue("Formats is empty array", validator.formats.length == 0);
			assertTrue("Country code is null", validator.countryCode == null);
		}
		
		[Test]
		public function setFormats():void {
			validator.format = "NNNN";
			assertTrue("Format is correct", validator.format = "NNNN");
			assertTrue("Formats length is correct", validator.formats.length = 1);
			assertTrue("Formats is correct", validator.formats[0] = "NNNN");
			
			validator.formats = ["NNNN", "NNNNNN"];
			assertTrue("Format is null", validator.format == null);
			assertTrue("Formats length is correct", validator.formats.length = 2);
			assertTrue("First format is correct", validator.formats[0] = "NNNN");
			assertTrue("Second format is correct", validator.formats[1] = "NNNNNN");
		}
		
		[Test]
		public function countryCode():void {
			validator.countryCode = "AU";
			assertTrue("Country code is correct", validator.countryCode = "AU");
		}
		
		
		[Test]
		public function fixedNumericPostcode():void {
			var results:Array;
			
			validator.format = "NNNN";
			
			results = PostCodeValidator.validatePostCode(validator, "1234", null);
			assertTrue("No errors", results.length == 0);
			
			results = PostCodeValidator.validatePostCode(validator, "1-23", null);
			assertTrue("Invalid Postcode", results.length == 1);
			
			results = PostCodeValidator.validatePostCode(validator, "123", null);
			assertTrue("Invalid Postcode", results.length == 1);
			
			results = PostCodeValidator.validatePostCode(validator, "123456", null);
			assertTrue("Invalid Postcode", results.length == 1);
			
			results = PostCodeValidator.validatePostCode(validator, "123D", null);
			assertTrue("Invalid Postcode", results.length == 1);
			
			results = PostCodeValidator.validatePostCode(validator, "1234D", null);
			assertTrue("Invalid Postcode", results.length == 2);		
		}
		
		[Test]
		public function multpleNumericPostcodes():void {
			var results:Array;
			
			validator.formats = ["NNNN", "NNNNNN"];
			
			results = PostCodeValidator.validatePostCode(validator, "1234", null);
			assertTrue("No errors", results.length == 0);
			
			results = PostCodeValidator.validatePostCode(validator, "123456", null);
			assertTrue("No errors", results.length == 0);
			
			results = PostCodeValidator.validatePostCode(validator, "1-23", null);
			assertTrue("Invalid Postcode", results.length == 1);
			
			results = PostCodeValidator.validatePostCode(validator, "123", null);
			assertTrue("Invalid Postcode", results.length == 1);
			
			results = PostCodeValidator.validatePostCode(validator, "12345", null);
			assertTrue("Invalid Postcode", results.length == 1);
			
			results = PostCodeValidator.validatePostCode(validator, "1234567", null);
			assertTrue("Invalid Postcode", results.length == 1);
			
			results = PostCodeValidator.validatePostCode(validator, "123D", null);
			assertTrue("Invalid Postcode", results.length == 1);
			
			results = PostCodeValidator.validatePostCode(validator, "1F234", null);
			assertTrue("Invalid Postcode", results.length == 2);		
		}
		
		[Test]
		public function countryCodePostcode():void {
			var results:Array;
			
			validator.format = "CCNNNN";
			validator.countryCode = "AA";
			
			results = PostCodeValidator.validatePostCode(validator, "AA1234", null);
			assertTrue("No errors", results.length == 0);
			
			results = PostCodeValidator.validatePostCode(validator, "BB1234", null);
			assertTrue("Invalid Postcode", results.length == 1);
			
			results = PostCodeValidator.validatePostCode(validator, "AA123", null);
			assertTrue("Invalid Postcode", results.length == 1);
			
			results = PostCodeValidator.validatePostCode(validator, "AA12345", null);
			assertTrue("Invalid Postcode", results.length == 1);
			
			results = PostCodeValidator.validatePostCode(validator, "1234AA", null);
			assertTrue("Invalid Postcode", results.length == 1);
			
			results = PostCodeValidator.validatePostCode(validator, "A1A234", null);
			assertTrue("Invalid Postcode", results.length == 1);
		}
		
		[Test]
		public function spacingInPostcodes():void {
			var results:Array;
			
			validator.format = "AA-NN NN";
			
			results = PostCodeValidator.validatePostCode(validator, "AB-12 34", null);
			assertTrue("No errors", results.length == 0);
			
			results = PostCodeValidator.validatePostCode(validator, "AB 12 34", null);
			assertTrue("Invalid Postcode", results.length == 1);
			
			results = PostCodeValidator.validatePostCode(validator, "AB-12-34", null);
			assertTrue("Invalid Postcode", results.length == 1);
			
			results = PostCodeValidator.validatePostCode(validator, "AB-12  34", null);
			assertTrue("Invalid Postcode", results.length == 1);
		}
		
		[Test]
		public function alaphaNumericPostcode():void {
			var results:Array;
			
			validator.format = "NNNN AA";
			
			results = PostCodeValidator.validatePostCode(validator, "1234 AB", null);
			assertTrue("No errors", results.length == 0);
			
			results = PostCodeValidator.validatePostCode(validator, "1234-AB", null);
			assertTrue("Invalid Postcode", results.length == 1);
			
			results = PostCodeValidator.validatePostCode(validator, "AB 1234", null);
			assertTrue("Invalid Postcode", results.length == 1);
			
			results = PostCodeValidator.validatePostCode(validator, "12345AB", null);
			assertTrue("Invalid Postcode", results.length == 1);
			
			results = PostCodeValidator.validatePostCode(validator, "1234ABC", null);
			assertTrue("Invalid Postcode", results.length == 1);
			
			results = PostCodeValidator.validatePostCode(validator, "12345 AB", null);
			assertTrue("Invalid Postcode", results.length == 1);
			
			results = PostCodeValidator.validatePostCode(validator, "123 AB", null);
			assertTrue("Invalid Postcode", results.length == 1);
		}
		
	}
}