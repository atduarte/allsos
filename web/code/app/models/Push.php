<?php

namespace AllSOS\Models;

class Push
{
    const API_ACCESS_KEY = 'AIzaSyAPS4yV7FE18NCbvpne3PxMlz0Ddv7K9_I';

    public static function send($message, $ids)
    {
        $msg = [
            'message'    => $message,
            'title'      => 'This is a title. title',
            'subtitle'   => 'This is a subtitle. subtitle',
            'tickerText' => 'Ticker text here...Ticker text here...Ticker text here',
            'vibrate'    => 1,
            'sound'      => 1
        ];

        $fields = [
            'registration_ids'  => $ids,
            'data'              => $msg
        ];

        $headers = [
            'Authorization: key=' . self::API_ACCESS_KEY,
            'Content-Type: application/json'
        ];

        $ch = curl_init();
        curl_setopt( $ch,CURLOPT_URL, 'https://android.googleapis.com/gcm/send' );
        curl_setopt( $ch,CURLOPT_POST, true );
        curl_setopt( $ch,CURLOPT_HTTPHEADER, $headers );
        curl_setopt( $ch,CURLOPT_RETURNTRANSFER, true );
        curl_setopt( $ch,CURLOPT_SSL_VERIFYPEER, false );
        curl_setopt( $ch,CURLOPT_POSTFIELDS, json_encode( $fields ) );
        $result = curl_exec($ch );
        curl_close( $ch );

        return $result;
    }

}
