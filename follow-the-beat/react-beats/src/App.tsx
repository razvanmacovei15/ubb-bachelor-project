
import './App.css'
import TopBar from './components/TopBar'
import HomePage from './pages/HomePage'
function App() {

  return (
    <div className='w-full h-screen flex flex-col'>
      <TopBar/>
      <HomePage/>
    </div>
  )
}

export default App
