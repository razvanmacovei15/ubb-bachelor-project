import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import {createBrowserRouter, RouterProvider} from "react-router-dom";
import App from './App.tsx';
import HomePage from './pages/HomePage.tsx';
import Profile from './pages/ProfilePage.tsx';
import Lineup from './pages/LineupPage.tsx';
import { ConcertProvider } from './components/contexts/ConcertContext.tsx';
import { LineupProvider } from './components/contexts/LineupContext.tsx';
import ConcertsPage from './pages/ConcertsPage.tsx';


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
            path: "concerts",
            element: <ConcertsPage />,
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
    <ConcertProvider>
      <LineupProvider>
        <RouterProvider router={router} />
      </LineupProvider>
    </ConcertProvider>
  </StrictMode>
)
