////////////////////////////////////////////////////////////////////////////////
//
//  Licensed to the Apache Software Foundation (ASF) under one or more
//  contributor license agreements.  See the NOTICE file distributed with
//  this work for additional information regarding copyright ownership.
//  The ASF licenses this file to You under the Apache License, Version 2.0
//  (the "License"); you may not use this file except in compliance with
//  the License.  You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
//  Unless required by applicable law or agreed to in writing, software
//  distributed under the License is distributed on an "AS IS" BASIS,
//  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//  See the License for the specific language governing permissions and
//  limitations under the License.
//
////////////////////////////////////////////////////////////////////////////////

package mx.collections.tests.vectorList {
	import flash.events.EventDispatcher;
	
	import mx.collections.VectorList;
	import mx.events.PropertyChangeEvent;
	
	import org.flexunit.assertThat;
	import org.flexunit.asserts.assertEquals;
	import org.flexunit.asserts.assertFalse;
	import org.flexunit.asserts.assertTrue;
	import org.hamcrest.collection.hasItem;
	import org.hamcrest.core.not;

	public class AddRemoveItemAtTest {
		
		[Test]
		public function addItemAtShouldInsertElementIntoVector():void {
			var vector:Vector.<int> = new <int>[ 7, 8, 9 ];
			var vectorList:VectorList = new VectorList( vector );
			const value:int = 5;
			
			vectorList.addItemAt( value, 2 );
			assertEquals( 4, vectorList.length );
			assertEquals( value, vector[ 2 ] );
		}

		public function addItemAtShouldAddEventListenerForEventDispatchers():void {
			const value:EventDispatcher = new EventDispatcher();
			var vector:Vector.<EventDispatcher> = new <EventDispatcher>[value];
			var vectorList:VectorList = new VectorList( vector );
			
			assertFalse( value.hasEventListener( PropertyChangeEvent.PROPERTY_CHANGE ) );
			vectorList.addItemAt( value, 0 );
			assertTrue( value.hasEventListener( PropertyChangeEvent.PROPERTY_CHANGE ) );
		}

		[Test(expects="RangeError")]
		public function shouldReceiveRangeErrorAttemptingToAddItemAtNegativeIndex():void {
			var vector:Vector.<int> = new <int>[];
			var vectorList:VectorList = new VectorList( vector );
			const value:int = 5;
			
			vectorList.addItemAt( value, -1 );
		}
		
		[Test(expects="RangeError")]
		public function shouldReceiveRangeErrorAttemptingToAddItemAtPastEndOfVector():void {
			var vector:Vector.<int> = new <int>[];
			var vectorList:VectorList = new VectorList( vector );
			const value:int = 5;
			
			vectorList.addItemAt( value, 2 );
		}
		
		[Test(expects="RangeError")]
		public function shouldReceiveErrorAttemptingToAddItemAtToFixedLengthVector():void {
			var vector:Vector.<int> = new Vector.<int>( 1, true );
			var vectorList:VectorList = new VectorList( vector );
			const value:int = 5;
			
			vectorList.addItemAt( value, 0 );
		}

		[Test(expects="RangeError")]
		public function shouldReceiveErrorAttemptingToRemoveItemAtToFixedLengthVector():void {
			var vector:Vector.<int> = new Vector.<int>( 1, true );
			var vectorList:VectorList = new VectorList( vector );
			
			vectorList.removeItemAt( 0 );
		}

		[Test]
		public function removeItemAtShouldRemoveItemFromVector():void {
			const value:int = 5;
			var vector:Vector.<int> = new <int>[ value ];
			var vectorList:VectorList = new VectorList( vector );
			
			assertThat( vector, hasItem( value ) );
			
			vectorList.removeItemAt( 0 );
			assertThat( vector, not( hasItem( value ) ) );
		}

		public function removeItemAtShouldRemoveEventListenerForEventDispatchers():void {
			const value:EventDispatcher = new EventDispatcher();
			var vector:Vector.<EventDispatcher> = new <EventDispatcher>[value];
			var vectorList:VectorList = new VectorList( vector );
			
			assertTrue( value.hasEventListener( PropertyChangeEvent.PROPERTY_CHANGE ) );
			vectorList.removeItemAt( 0 );
			assertFalse( value.hasEventListener( PropertyChangeEvent.PROPERTY_CHANGE ) );
		}
		
		[Test]
		public function removeItemAtShouldRemoveReturnRemovedItem():void {
			const value:int = 5;
			var vector:Vector.<int> = new <int>[ value ];
			var vectorList:VectorList = new VectorList( vector );
			
			assertEquals( value, vectorList.removeItemAt( 0 ) );
		}

		[Test]
		public function removeItemAtShouldRemoveArbitraryNode():void {
			const value:int = 5;
			var vector:Vector.<int> = new <int>[ 3, 7, 9, value, 11, 13 ];
			var vectorList:VectorList = new VectorList( vector );
			
			assertThat( vector, hasItem( value ) );
			
			vectorList.removeItemAt( 3 );

			assertThat( vector, not( hasItem( value ) ) );
		}

		[Test]
		public function removeItemAtShouldRemoveLastNode():void {
			const value:int = 5;
			var vector:Vector.<int> = new <int>[ 3, 7, 9, 11, 13, value ];
			var vectorList:VectorList = new VectorList( vector );
			
			assertThat( vector, hasItem( value ) );
			
			vectorList.removeItemAt( 5 );
			
			assertThat( vector, not( hasItem( value ) ) );
		}

		[Test(expects="RangeError")]
		public function removeItemAtShouldThrowRangeErrorIfOutOfRangeIndexSupplied():void {
			const value:int = 5;
			var vector:Vector.<int> = new <int>[ value ];
			var vectorList:VectorList = new VectorList( vector );

			vectorList.removeItemAt( 3 );
		}

		public function AddRemoveItemAtTest() {
		}
	}
}