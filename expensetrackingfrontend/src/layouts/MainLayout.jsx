import React from 'react'
import { Outlet } from 'react-router-dom'
import CustomNavbar from '../components/CustomNavbar'

const MainLayout = () => {
  return (
    <>
        {/* <CustomNavbar /> */}
        <Outlet />
    </>
  )
}

export default MainLayout