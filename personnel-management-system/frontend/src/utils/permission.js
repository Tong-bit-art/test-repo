import { ElMessage } from 'element-plus'

export const hasPermission = (requiredRole) => {
  const user = JSON.parse(localStorage.getItem('user') || '{}')
  if (!user.roles || user.roles.length === 0) {
    return false
  }
  return user.roles.some(role => role.name === requiredRole)
}

export const hasAnyPermission = (requiredRoles) => {
  const user = JSON.parse(localStorage.getItem('user') || '{}')
  if (!user.roles || user.roles.length === 0) {
    return false
  }
  return user.roles.some(role => requiredRoles.includes(role.name))
}

export const isAdmin = () => {
  return hasPermission('ADMIN')
}

export const isEntry = () => {
  return hasPermission('ENTRY')
}

export const checkPermission = (requiredRole) => {
  if (!hasPermission(requiredRole)) {
    ElMessage.error('权限不足')
    return false
  }
  return true
}

export const checkAnyPermission = (requiredRoles) => {
  if (!hasAnyPermission(requiredRoles)) {
    ElMessage.error('权限不足')
    return false
  }
  return true
}

export const getUserRoles = () => {
  const user = JSON.parse(localStorage.getItem('user') || '{}')
  return user.roles || []
}

export const getUsername = () => {
  const user = JSON.parse(localStorage.getItem('user') || '{}')
  return user.username || ''
}

export const getUserName = () => {
  const user = JSON.parse(localStorage.getItem('user') || '{}')
  return user.name || ''
}

export const getUserId = () => {
  const user = JSON.parse(localStorage.getItem('user') || '{}')
  return user.id || null
}