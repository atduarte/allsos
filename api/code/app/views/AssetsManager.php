<?php

namespace Notnull\Revis\Models;

use \Phalcon\Assets\Manager as PhManager;
use \Phalcon\Tag;

/**
 * Add Asset caching
 *
 * @author   Olivier Garbé <ogarbe@voyageprive.com>
 */
class AssetsManager extends PhManager
{
    /**
     * Prints the HTML for JS resources
     *
     * @param string $collectionName the name of the collection
     *
     * @return string the result of the collection
     **/
    public function outputJs($collectionName = null)
    {
        $collection = $this->collection($collectionName);

        if ($collection->getJoin()) {
            $filename = $collection->getTargetPath();

            if (file_exists($filename)) {
                $time = 0;
                foreach ($collection->getResources() as $resource) {
                    $tmp = filemtime($resource->getRealTargetPath());
                    if ($tmp > $time) {
                        $time = $tmp;
                    }
                }

                // If created
                if (filemtime($filename) > $time) {
                    $collection->setTargetUri($collection->getTargetUri() . '?' . filemtime($filename));
                    return Tag::javascriptInclude($collection->getTargetUri());
                }
            }

            // Else CREATE
            $res = parent::outputJs($collectionName);
            return $res;
        }

        return parent::outputJs($collectionName);
    }

    /**
     * Prints the HTML for CSS resources
     *
     * @param string $collectionName the name of the collection
     *
     * @return string the result of the collection
     **/
    public function outputCss($collectionName = null)
    {
        $collection = $this->collection($collectionName);

        if ($collection->getJoin()) {
            $filename = $collection->getTargetPath();

            if (file_exists($filename)) {
                $time = 0;
                foreach ($collection->getResources() as $resource) {
                    $tmp = filemtime($resource->getRealTargetPath());
                    if ($tmp > $time) {
                        $time = $tmp;
                    }
                }

                // If created
                if (filemtime($filename) > $time) {
                    $collection->setTargetUri($collection->getTargetUri() . '?' . filemtime($filename));
                    return Tag::stylesheetLink($collection->getTargetUri());
                }
            }

            // Else CREATE
            $res = parent::outputCss($collectionName);
            return $res;
        }

        return parent::outputCss($collectionName);
    }
}
