import { Client } from "@stomp/stompjs";
import { useState, useEffect, useCallback } from "react";
import { json } from "react-router-dom";

const client = new Client({
  brokerURL: import.meta.env.VITE_BACKEND_RABBITMQ,
  logRawCommunication: true,
  connectHeaders: {
    login: import.meta.env.VITE_RABBITMQ_USER,
    passcode: import.meta.env.VITE_RABBITMQ_PASS,
  },
});

interface LiveDataProps {
  msg: string;
}

export const useLiveData = () => {
  const [error, serError] = useState<Error | null>(null);
  const [isConnected, setIsConnected] = useState(false);
  const [data, setData] = useState<LiveDataProps>();
  // const [sendData, setSendData] = useState<LiveDataProps>();

  const sendData = useCallback((msg:string)=>{
    client.publish({
      destination:"/topic/msg",
      body:JSON.stringify({msg:msg})
    })
  },[client, ])


  // On connect, subscribe to the lectureId and languageCode topics
  client.onConnect = () => {
    console.log("connected");
    setIsConnected(true);

    client.subscribe(`/topic/msg`, (m) => {
      console.log("DATA", {m});
      const msg: LiveDataProps = JSON.parse(m.body);
      setData(msg);
      
    });
  };

  client.onDisconnect = () => {
    console.log("disconnected");
    setIsConnected(false);
  };

  client.onStompError = (err) => {
    console.log("error", {err});
    serError(new Error(err.body));
  };

  client.onChangeState = (state) => {
    console.log({ state });
  };

  client.onWebSocketError = (err) => {
    console.log({ err });
  };

  client.onUnhandledFrame = (frame) => {
    console.log({ frame });
  };

  client.activate();

  useEffect(() => {
    return () => {
      client.deactivate();
    };
  }, []);

  return {
    isConnected,
    data,
    error,
    sendData
  };
};
