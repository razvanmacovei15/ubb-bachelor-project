import React, {
  createContext,
  useContext,
  useState,
  useEffect,
  ReactNode,
} from "react";
import axios from "axios";

interface UserContextType {
  isConnectedToSpotify: boolean;
  sessionToken: string | null;
  checkSpotifyConnection: () => Promise<boolean>;
  isLoading: boolean;
  setConnectionState: (isConnected: boolean, token: string) => void;
}

const defaultContextValue: UserContextType = {
  isConnectedToSpotify: false,
  sessionToken: null,
  checkSpotifyConnection: async () => false,
  isLoading: true,
  setConnectionState: () => {},
};

const UserContext = createContext<UserContextType>(defaultContextValue);

interface UserProviderProps {
  children: ReactNode;
}

export const UserProvider: React.FC<UserProviderProps> = ({ children }) => {
  const [isConnectedToSpotify, setIsConnectedToSpotify] =
    useState<boolean>(false);
  const [isLoading, setIsLoading] = useState<boolean>(true);
  const [sessionToken, setSessionToken] = useState<string | null>(null);

  const API_URL = "";
  // const API_URL = import.meta.env.VITE_API_URL;

  useEffect(() => {
    const token = localStorage.getItem("sessionToken");
    if (token) {
      setSessionToken(token);
    }
  }, []);

  const checkSpotifyConnection = async () => {
    if (!sessionToken) {
      setIsConnectedToSpotify(false);
      return false;
    }

    try {
      const response = await axios.get<boolean>(
        `${API_URL}/spotify-auth/is-connected`,
        { params: { sessionToken } }
      );
      setIsConnectedToSpotify(response.data);
      return response.data;
    } catch (err) {
      console.error("Error checking Spotify connection:", err);
      setIsConnectedToSpotify(false);
      return false;
    }
  };

  useEffect(() => {
    console.log(
      "[UserContext] useEffect mount, token from localStorage:",
      localStorage.getItem("sessionToken")
    );
  }, []);

  const setConnectionState = (isConnected: boolean, token: string) => {
    console.log("[UserContext] setConnectionState called with:", {
      isConnected,
      token,
    });
    setIsConnectedToSpotify(isConnected);
    setSessionToken(token);
    localStorage.setItem("sessionToken", token);
    axios.defaults.headers.common["Authorization"] = `Bearer ${token}`;
  };

  useEffect(() => {
    const initializeUserState = async () => {
      setIsLoading(true);
      if (sessionToken) {
        await checkSpotifyConnection();
      }
      setIsLoading(false);
    };

    initializeUserState();
  }, [sessionToken]);

  const value = {
    isConnectedToSpotify,
    sessionToken,
    checkSpotifyConnection,
    isLoading,
    setConnectionState,
  };

  return <UserContext.Provider value={value}>{children}</UserContext.Provider>;
};

export const useUser = () => {
  const context = useContext(UserContext);
  if (!context) {
    throw new Error("useUser must be used within a UserProvider");
  }
  return context;
};
