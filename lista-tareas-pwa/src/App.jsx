import { useState, useEffect } from 'react'

function App() {
  const [tasks, setTasks] = useState([])
  const [newTask, setNewTask] = useState('')

  // 1. Cargar tareas desde localStorage al inicio
  useEffect(() => {
    const savedTasks = localStorage.getItem('tasks')
    if (savedTasks) setTasks(JSON.parse(savedTasks))
    
    // Registrar Service Worker para PWA
    if ('serviceWorker' in navigator) {
      navigator.serviceWorker.register('/sw.js')
        .then(registration => {
          console.log('Service Worker registrado con éxito:', registration.scope)
          registration.update() // Forzar actualización del SW
        })
        .catch(err => {
          console.log('Error al registrar Service Worker:', err)
        })
    }
    
    // Solicitar permisos para notificaciones
    if ('Notification' in window) {
      Notification.requestPermission().then(permission => {
        if (permission === 'granted') {
          console.log('Permiso para notificaciones concedido')
          showWelcomeNotification()
        }
      })
    }
  }, [])

  // 2. Guardar tareas cuando cambian
  useEffect(() => {
    localStorage.setItem('tasks', JSON.stringify(tasks))
    checkPendingTasksNotification()
  }, [tasks])

  // 3. Funciones principales
  const addTask = () => {
    if (newTask.trim() === '') return
    const newTaskObj = {
      id: Date.now(),
      text: newTask,
      completed: false,
      createdAt: new Date().toISOString()
    }
    setTasks([...tasks, newTaskObj])
    setNewTask('')
    showTaskAddedNotification(newTaskObj.text)
  }

  const toggleTask = (id) => {
    setTasks(tasks.map(task => 
      task.id === id ? { ...task, completed: !task.completed } : task
    ))
  }

  const deleteTask = (id) => {
    setTasks(tasks.filter(task => task.id !== id))
  }

  // 4. Funciones de notificaciones PWA
  const showWelcomeNotification = () => {
    if (Notification.permission === 'granted' && tasks.length === 0) {
      new Notification('¡Bienvenido a tu Lista de Tareas PWA!', {
        body: 'Comienza añadiendo tu primera tarea',
        icon: '/pwa-192x192.png',
        vibrate: [200, 100, 200]
      })
    }
  }

  const showTaskAddedNotification = (taskText) => {
    if (Notification.permission === 'granted') {
      new Notification('Tarea añadida', {
        body: `"${taskText}"`,
        icon: '/pwa-192x192.png'
      })
    }
  }

  const checkPendingTasksNotification = () => {
    const pendingTasks = tasks.filter(t => !t.completed)
    if (pendingTasks.length > 0 && Notification.permission === 'granted') {
      // Solo notificar si hay tareas pendientes por más de 1 hora
      const oldPendingTasks = pendingTasks.filter(task => {
        const taskDate = new Date(task.createdAt)
        const hoursDiff = (new Date() - taskDate) / (1000 * 60 * 60)
        return hoursDiff >= 1
      })

      if (oldPendingTasks.length > 0) {
        new Notification('📌 Tareas pendientes', {
          body: `Tienes ${oldPendingTasks.length} tareas sin completar`,
          icon: '/pwa-192x192.png',
          tag: 'pending-tasks' // Evita notificaciones duplicadas
        })
      }
    }
  }

  // 5. Verificar tareas pendientes periódicamente
  useEffect(() => {
    const interval = setInterval(() => {
      checkPendingTasksNotification()
    }, 3600000) // Cada hora

    return () => clearInterval(interval)
  }, [tasks])

  return (
    <div className="min-h-screen bg-gray-100 p-6">
      <div className="max-w-md mx-auto bg-white rounded-xl shadow-md overflow-hidden p-6">
        <h1 className="text-2xl font-bold text-gray-800 mb-6">Lista de Tareas PWA</h1>
        <p className="text-sm text-blue-500 mb-4">✓ Funciona offline ✓ Instalable ✓ Notificaciones</p>
        
        {/* Formulario para añadir tareas */}
        <div className="flex mb-6">
          <input
            type="text"
            value={newTask}
            onChange={(e) => setNewTask(e.target.value)}
            onKeyPress={(e) => e.key === 'Enter' && addTask()}
            placeholder="Escribe una nueva tarea"
            className="flex-grow px-4 py-2 border rounded-l-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
          />
          <button
            onClick={addTask}
            className="bg-blue-500 hover:bg-blue-600 text-white px-4 py-2 rounded-r-lg transition duration-200"
            aria-label="Añadir tarea"
          >
            Añadir
          </button>
        </div>

        {/* Lista de tareas */}
        {tasks.length > 0 ? (
          <ul className="space-y-3">
            {tasks.map(task => (
              <li 
                key={task.id} 
                className={`flex items-center justify-between p-3 rounded-lg ${
                  task.completed ? 'bg-green-50' : 'bg-gray-50'
                }`}
              >
                <div className="flex items-center">
                  <input
                    type="checkbox"
                    checked={task.completed}
                    onChange={() => toggleTask(task.id)}
                    className="h-5 w-5 text-blue-500 rounded focus:ring-blue-400 mr-3"
                    aria-label={task.completed ? 'Marcar como pendiente' : 'Marcar como completada'}
                  />
                  <span className={`${task.completed ? 'line-through text-gray-400' : 'text-gray-800'}`}>
                    {task.text}
                  </span>
                </div>
                <button
                  onClick={() => deleteTask(task.id)}
                  className="text-red-500 hover:text-red-700 p-1"
                  aria-label="Eliminar tarea"
                >
                  🗑️
                </button>
              </li>
            ))}
          </ul>
        ) : (
          <p className="text-gray-500 text-center py-4">No hay tareas añadidas</p>
        )}

        {/* Contador de tareas y botón de instalación PWA */}
        <div className="mt-6 flex justify-between items-center">
          <span className="text-sm text-gray-500">
            {tasks.filter(t => !t.completed).length} tareas pendientes
          </span>
          <button
            onClick={() => {
              if (window.deferredPrompt) {
                window.deferredPrompt.prompt()
                window.deferredPrompt.userChoice.then(choiceResult => {
                  if (choiceResult.outcome === 'accepted') {
                    console.log('Usuario instaló la PWA')
                  }
                  window.deferredPrompt = null
                })
              }
            }}
            id="installButton"
            className="hidden text-sm bg-green-500 hover:bg-green-600 text-white px-3 py-1 rounded"
          >
            Instalar App
          </button>
        </div>
      </div>
    </div>
  )
}

export default App