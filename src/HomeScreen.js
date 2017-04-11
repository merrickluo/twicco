import React from 'react'
import {
  StyleSheet,
  View,
  Text,
} from 'react-native'

/* import TwitterKit from 'react-native-fabric-twitterkit'
 * import LinearGradient from 'react-native-linear-gradient'*/

import BaseScreen from './BaseScreen.js'
import { connect } from 'react-redux'

@connect(state => {
  return {
    account: state.app.account,
    api: state.api.twitter,
  }
})
export default class HomeScreen extends BaseScreen {
  componentDidMount() {
    this.props.api.rest.get('statuses/home_timeline')
        .then(result => {
          console.log(result)
        })
        .catch(e => {
          console.log(e)
        })
  }

  render() {
    return (
      <View style={styles.container}>
        <View style={styles.content}>
          <Text>Home</Text>
        </View>
      </View>
    )
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    flexDirection: 'column',
  },
  content: {
    flex: 1,
  },
})
