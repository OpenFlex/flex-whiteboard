/*
 * ////////////////////////////////////////////////////////////////////////////////
 * //
 * //  Licensed to the Apache Software Foundation (ASF) under one or more
 * //  contributor license agreements.  See the NOTICE file distributed with
 * //  this work for additional information regarding copyright ownership.
 * //  The ASF licenses this file to You under the Apache License, Version 2.0
 * //  (the "License"); you may not use this file except in compliance with
 * //  the License.  You may obtain a copy of the License at
 * //
 * //      http://www.apache.org/licenses/LICENSE-2.0
 * //
 * //  Unless required by applicable law or agreed to in writing, software
 * //  distributed under the License is distributed on an "AS IS" BASIS,
 * //  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * //  See the License for the specific language governing permissions and
 * //  limitations under the License.
 * //
 * ////////////////////////////////////////////////////////////////////////////////
 */


package org.apache.flex.classloader
{
import flex.classloader.*;

import flash.display.Loader;

import flash.events.Event;
import flash.events.IOErrorEvent;
import flash.events.SecurityErrorEvent;
import flash.net.URLRequest;
import flash.system.ApplicationDomain;
import flash.system.LoaderContext;

/**
 * Implements a Class Loader which loads Classes within a SWF container from a URL
 */
public class SWFClassLoader extends ClassLoader
{
    /**
     * Constructor
     */
    public function SWFClassLoader(swfURL:String, parent:ClassLoader = null, domain:ApplicationDomain = null)
    {
        super(parent, domain);

        this.swfURL = swfURL;
    }


    /* PUBLIC */


    /**
     * Load the classes from SWF
     */
    override protected function loadContainer():void
    {
        loader = new Loader();
        loader.contentLoaderInfo.addEventListener(Event.COMPLETE, completeHandler);
        loader.contentLoaderInfo.addEventListener(IOErrorEvent.IO_ERROR, ioErrorHandler);
        loader.contentLoaderInfo.addEventListener(SecurityErrorEvent.SECURITY_ERROR, securityErrorHandler);
        loader.contentLoaderInfo.addEventListener(Event.UNLOAD, unloadHandler);

        const request:URLRequest = new URLRequest(swfURL);
        const context:LoaderContext = new LoaderContext(false, _domain);

        loader.load(request, context);
    }


    /**
     *  UnLoad the classes from their container e.g. SWF/SWC etc
     */
    override protected function unloadContainer():void
    {
        if (_domain)
        {
            _domain = null;
        }

        if (loader)
        {
            loader.contentLoaderInfo.removeEventListener(Event.COMPLETE, completeHandler);
            loader.contentLoaderInfo.removeEventListener(IOErrorEvent.IO_ERROR, ioErrorHandler);
            loader.contentLoaderInfo.removeEventListener(SecurityErrorEvent.SECURITY_ERROR, securityErrorHandler);

            loader.unloadAndStop(true);
            loader = null;
        }
    }


    /**
     * Find a class form the SWF, and return it
     *
     * @throws ReferenceError
     */
    override protected function findDefinition(name:String):Object
    {
        try
        {
            return loader.contentLoaderInfo.applicationDomain.getDefinition(name);
        }
        catch (error:Error)
        {
            throw new ReferenceError(swfURL);
        }

        return null;
    }


    /* PRIVATE */

    private var loader:Loader;

    private var swfURL:String;

    /**
     * Handle load complete
     *
     * @param event
     */
    private function completeHandler(event:Event):void
    {
        _loaded = true;

        dispatchEvent(new ClassLoaderEvent(ClassLoaderEvent.CLASS_CONTAINER_LOADED));
    }

    /**
     * Handle load error
     *
     * @param event
     */
    private function ioErrorHandler(event:IOErrorEvent):void
    {
        dispatchEvent(new ClassLoaderEvent(ClassLoaderEvent.CLASS_CONTAINER_ERROR, event.text));
    }

    /**
     * Handle load security error
     *
     * @param event
     */
    private function securityErrorHandler(event:SecurityErrorEvent):void
    {
        dispatchEvent(new ClassLoaderEvent(ClassLoaderEvent.CLASS_CONTAINER_ERROR, event.text));
    }


    /**
     * Handle unload
     *
     * @param event
     */
    private function unloadHandler(event:Event):void
    {
        dispatchEvent(new ClassLoaderEvent(ClassLoaderEvent.CLASS_CONTAINER_UNLOADED));

        if (loader)
        {
            loader.removeEventListener(Event.UNLOAD, unloadHandler);
        }
    }
}
}
