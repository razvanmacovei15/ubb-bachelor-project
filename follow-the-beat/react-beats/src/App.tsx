
import { Outlet } from 'react-router-dom'
import './App.css'
import TopBar from './components/TopBar'
function App() {

  return (
    <div className='w-full h-screen flex flex-col'>
      <TopBar/>
      <Outlet/>
    </div>
  )
}

export default App
