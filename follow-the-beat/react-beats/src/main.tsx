import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import {createBrowserRouter, RouterProvider} from "react-router-dom";
import App from './App.tsx';
import HomePage from './pages/HomePage.tsx';
import Profile from './pages/Profile.tsx';
import Lineup from './pages/Lineup.tsx';


const router = createBrowserRouter([
    {
        path: "",
        element: <App />,
        children: [
          {
            index: true, // This makes Dashboard the default when visiting /gymapp
            element: <HomePage />, 
          },
          {
            path: "profile",
            element: <Profile />,
          },
          {
            path: "lineup",
            element: <Lineup />,
          }
        ]
    }
])

createRoot(document.getElementById('root')!).render(
  <StrictMode>
    <RouterProvider router={router} />
  </StrictMode>
)
